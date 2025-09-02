package com.sfh.model;

import org.springframework.ai.chat.client.ChatClient;

public interface ModelSelectService {
    ChatClient getClient(String modelName);
}
