package com.sfh.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    public static final String prompt = "你是一个股票AI助手，可以获取准确、完整的证券历史行情数据、上市公司财务数据，并且可以根据行情提供稳健的投资建议";
    private final OpenAiChatModel baseChatModel;
    private final ChatMemory chatMemory;
    private final ToolCallbackProvider toolCallbackProvider;



    public ChatClientConfig(OpenAiChatModel baseChatModel, ChatMemory chatMemory, ToolCallbackProvider toolCallbackProvider) {
        this.baseChatModel = baseChatModel;
        this.chatMemory = chatMemory;
        this.toolCallbackProvider = toolCallbackProvider;
    }


    @Value("${OPENAI_API_KEY}")
    private String openaiApiKey;

    @Value("${OPENAI_BASE_URL}")
    private String openaiBaseUrl;

    @Value("${OPENAI_MODEL}")
    private String openaiModel;

    @Value("${DEEPSEEK_API_KEY}")
    private String deepseekApiKey;

    @Value("${DEEPSEEK_BASE_URL}")
    private String deepseekBaseUrl;

    @Value("${DEEPSEEK_MODEL}")
    private String deepseekModel;



    @Bean
    public ChatClient gptminiClient() {
        OpenAiApi openAiApi = new OpenAiApi.Builder()
                .baseUrl(openaiBaseUrl)
                .apiKey(openaiApiKey)
                .build();
        OpenAiChatModel model = baseChatModel.mutate()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder().model(openaiModel).temperature(0.01).build())
                .build();
        ChatClient build = ChatClient.builder(model).defaultSystem(prompt)
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()).build();
        return build;
    }

    @Bean
    public ChatClient deepseekClient() {
        OpenAiApi dsApi = new OpenAiApi.Builder()
                .baseUrl(deepseekBaseUrl)
                .apiKey(deepseekApiKey)
                .build();
        OpenAiChatModel model = baseChatModel.mutate()
                .openAiApi(dsApi)
                .defaultOptions(OpenAiChatOptions.builder().model(deepseekModel).temperature(0.01).build())
                .build();
        ChatClient build = ChatClient.builder(model).defaultSystem(prompt)
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()).build();
        return build;
    }

}
