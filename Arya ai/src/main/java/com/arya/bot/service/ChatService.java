package com.arya.bot.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.content.Media;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String askArya(String message, String persona) {
        return this.chatClient.prompt()
                .system("You are Arya. Persona: " + persona + ". Use your tools for tasks.")
                .user(message)
                .toolNames("launchApp", "sendWhatsAppMessage", "searchDatabase",
                        "getSystemStats", "createFile", "webSearch")
                .call()
                .content();
    }

    public String askAryaWithVision(String message, String persona, Resource imageResource) {
        return this.chatClient.prompt()
                .system("You are Arya. Persona: " + persona + ". Analyze the image.")
                .user(u -> u.text(message)
                        .media(new Media(MimeTypeUtils.IMAGE_PNG, imageResource))) // Using the stable constructor instead of the builder
                .toolNames("launchApp", "createFile", "getSystemStats")
                .call()
                .content();
    }
}