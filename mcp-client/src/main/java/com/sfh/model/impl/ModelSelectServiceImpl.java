package com.sfh.model.impl;

import com.sfh.model.ModelSelectService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ModelSelectServiceImpl implements ModelSelectService {

    @Autowired
    private Map<String, ChatClient> clientsMap;


    public ChatClient getClient(String modelName) {
        ChatClient client = clientsMap.get(modelName);
        if (client == null) {
            System.out.println("未找到模型: " + modelName + ", 使用默认模型: deepseekClient");
            client = clientsMap.get("deepseekClient");
        }
        return client;
    }
}
