package com.arya.bot.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String askArya(String message, String persona) {
        return this.chatClient.prompt()
                .system("You are Arya. Use 'webSearch' for real-time info, " +
                        "'searchDatabase' for local data, and 'createFile' for tasks.")
                .user(message)
                .toolNames("launchApp", "sendWhatsAppMessage", "searchDatabase",
                        "getSystemStats", "createFile", "webSearch")
                .call()
                .content();
    }
}