package com.arya.bot.service;

import com.arya.bot.model.ChatMessage;
import com.arya.bot.repository.ChatRepository;
import org.springframework.ai.chat.client.ChatClient;
 // Fixed: Import from .model instead of .content
import org.springframework.ai.content.Media;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final ChatRepository chatRepository;

    public ChatService(ChatClient.Builder builder, ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
        this.chatClient = builder
                .defaultOptions(OpenAiChatOptions.builder()
                        // Prevents the 'extra_body' error with Groq
                        .parallelToolCalls(false)
                        .build())
                .build();
    }
    public String askArya(String message, String persona) {
        // 1. Get the response from Arya without auto-tools
        String response = this.chatClient.prompt()
                .system("You are Arya. If you need to open an app, " +
                        "respond exactly with: CALL_TOOL:launchApp:appName. " +
                        "Example: CALL_TOOL:launchApp:calc")
                .user(message)
                .call()
                .content();

        // 2. LOGIC: Manually check if Arya wants to use a tool
        if (response.contains("CALL_TOOL:launchApp:")) {
            String appName = response.split(":")[2].trim();
            try {
                Runtime.getRuntime().exec("cmd /c start " + appName);
                response = "I have opened " + appName + " for you.";
            } catch (Exception e) {
                response = "Sorry, I couldn't open the app: " + e.getMessage();
            }
        }

        // 3. PERSISTENCE
        ChatMessage log = new ChatMessage();
        log.setUserMessage(message);
        log.setAryaResponse(response);
        chatRepository.save(log);

        return response;
    }

    public String askAryaWithVision(String message, String persona, Resource imageResource) {
        return this.chatClient.prompt()
                .system("You are Arya. Persona: " + persona + ". Analyze the provided image.")
                .user(u -> u.text(message)
                        .media(new Media(MimeTypeUtils.IMAGE_PNG, imageResource)))
                .toolNames("launchApp", "createFile", "getSystemStats")
                .call()
                .content();
    }

    public List<ChatMessage> getChatHistory() {
        return chatRepository.findAllByOrderByTimestampDesc();
    }
}