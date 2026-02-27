package com.arya.bot.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;

    // We build the client once and give it the "Toolbox"
    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultFunctions("sendWhatsAppMessage") // Add your tools here
                .build();
    }

    public String askArya(String message, String persona) {
        String systemPrompt = "Your name is Arya. You are an AI Agent acting as a " + persona + ". " +
                "You have access to tools. If a user asks you to do something, use your tools. " +
                "You speak Hindi, English, and Marathi fluently.";

        return this.chatClient.prompt()
                .system(systemPrompt)
                .user(message)
                .call()
                .content();
    }
}