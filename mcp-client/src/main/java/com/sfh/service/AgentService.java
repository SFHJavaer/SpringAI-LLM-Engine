package com.sfh.service;

import com.sfh.pojo.ChatEntity;
import org.springframework.ai.document.Document;

import java.util.List;

public interface AgentService {
    void chat(ChatEntity chatEntity);


    /**
     * @Description: Rag知识库检索汇总给大模型输出
     * @Author sfh
     * @param chatEntity
     * @param ragContext
     */
    void doChatRagSearch(ChatEntity chatEntity, List<Document> ragContext);


    /**
     * @Description: 基于searxng的实时联网搜索
     * @Author sfh
     * @param chatEntity
     */
    public void doInternetSearch(ChatEntity chatEntity);
}
