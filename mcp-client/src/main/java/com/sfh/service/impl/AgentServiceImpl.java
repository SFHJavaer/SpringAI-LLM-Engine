package com.sfh.service.impl;

import cn.hutool.json.JSONUtil;
import com.sfh.enums.SSEMsgType;
import com.sfh.model.ModelSelectService;
import com.sfh.pojo.ChatEntity;
import com.sfh.pojo.ChatResponseEntity;
import com.sfh.pojo.SearchResult;
import com.sfh.server.SSEServer;
import com.sfh.service.AgentService;
import com.sfh.service.SearXngService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AgentServiceImpl implements AgentService {

    private final ModelSelectService modelSelectService;

    private final SearXngService searXngService;



    public AgentServiceImpl(ModelSelectService modelSelectService, SearXngService searXngService) {
        this.modelSelectService = modelSelectService;
        this.searXngService = searXngService;
    }

    @Override
    public void chat(ChatEntity chatEntity) {
        String userId = chatEntity.getCurrentUserName();
        String prompt = chatEntity.getMessage();
        String botMsgId = chatEntity.getBotMsgId();
        String modelName = chatEntity.getModelName();

        // 处理modelName为空的情况，默认使用deepseekClient
        if (modelName == null || modelName.trim().isEmpty()) {
            System.out.println("modelName为空，使用默认模型: deepseekClient");
            modelName = "deepseekClient";
        }

        ChatClient chatClient = modelSelectService.getClient(modelName);


        Flux<String> stringFlux = chatClient.prompt().user(prompt).stream().content();

        List<String> list = stringFlux.toStream().map(content -> {
            SSEServer.sendMsg(userId, content, SSEMsgType.ADD);
            return content;
        }).toList();

        String fullContent = list.stream().collect(Collectors.joining());

        ChatResponseEntity chatResponseEntity = new ChatResponseEntity(fullContent, botMsgId);

        SSEServer.sendMsg(userId, JSONUtil.toJsonStr(chatResponseEntity), SSEMsgType.FINISH);

    }

    private static final String ragPROMPT = """
                                              基于上下文的知识库内容回答问题：
                                              【上下文】
                                              {context}
                                              
                                              【问题】
                                              {question}
                                              
                                              【输出】
                                              如果没有查到，请回复：不知道。
                                              如果查到，请回复具体的内容。不相关的近似内容不必提到。
                                              """;


    @Override
    public void doChatRagSearch(ChatEntity chatEntity, List<Document> ragContext) {

        String userId = chatEntity.getCurrentUserName();
        String question = chatEntity.getMessage();
        String botMsgId = chatEntity.getBotMsgId();
        String modelName = chatEntity.getModelName();

        // 处理modelName为空的情况，默认使用deepseekClient
        if (modelName == null || modelName.trim().isEmpty()) {
            System.out.println("RAG搜索中modelName为空，使用默认模型: deepseekClient");
            modelName = "deepseekClient";
        }

        System.out.println("RAG搜索使用模型: " + modelName);
        ChatClient chatClient = modelSelectService.getClient(modelName);
        // 构建提示词
        String context = "";
        if (ragContext != null && !ragContext.isEmpty()) {
            context = ragContext.stream()
                    .map(Document::getText)
                    .collect(Collectors.joining("\n"));
        }

        // 组装提示词
        Prompt prompt = new Prompt(ragPROMPT
                .replace("{context}", context)
                .replace("{question}", question));


        Flux<String> stringFlux = chatClient.prompt(prompt).stream().content();

        List<String> list = stringFlux.toStream().map(chatResponse -> {
            String content = chatResponse.toString();
            SSEServer.sendMsg(userId, content, SSEMsgType.ADD);
            log.info("content: {}", content);
            return content;
        }).toList();

        String fullContent = list.stream().collect(Collectors.joining());

        ChatResponseEntity chatResponseEntity = new ChatResponseEntity(fullContent, botMsgId);
        //前端根据FINISH对content进行format
        SSEServer.sendMsg(userId, JSONUtil.toJsonStr(chatResponseEntity), SSEMsgType.FINISH);

    }


    @Override
    public void doInternetSearch(ChatEntity chatEntity) {

        String userId = chatEntity.getCurrentUserName();
        String question = chatEntity.getMessage();
        String botMsgId = chatEntity.getBotMsgId();

        ChatClient chatClient = modelSelectService.getClient(chatEntity.getModelName());

        List<SearchResult> searchResults = searXngService.search(question);

        String finalPrompt = buildSesrXngPrompt(question, searchResults);

        // 组装提示词
        Prompt prompt = new Prompt(finalPrompt);


        Flux<String> stringFlux = chatClient.prompt(prompt).stream().content();

        List<String> list = stringFlux.toStream().map(chatResponse -> {
            String content = chatResponse.toString();
            SSEServer.sendMsg(userId, content, SSEMsgType.ADD);
            return content;
        }).toList();

        String fullContent = String.join("", list);

        ChatResponseEntity chatResponseEntity = new ChatResponseEntity(fullContent, botMsgId);

        SSEServer.sendMsg(userId, JSONUtil.toJsonStr(chatResponseEntity), SSEMsgType.FINISH);
    }

    private static String buildSesrXngPrompt(String question, List<SearchResult> searchResults) {

        StringBuilder context = new StringBuilder();

        searchResults.forEach(searchResult -> {
            context.append(
                    String.format("<context>\n[来源]: %s \n [摘要]: %s \n </context>\n",
                            searchResult.getUrl(),
                            searchResult.getContent()));
        });

        return sesrXngPROMPT
                .replace("{context}", context)
                .replace("{question}", question);
    }


    private static final String sesrXngPROMPT = """
                                              你是一个互联网搜索大师，请基于以下互联网返回的结果作为上下文，根据你的理解结合用户的提问综合后，生成并且输出专业的回答：
                                              【上下文】
                                              {context}
                                              
                                              【问题】
                                              {question}
                                              
                                              【输出】
                                              如果没有查到，请回复：不知道。
                                              如果查到，请回复具体的内容。
                                              """;
}
