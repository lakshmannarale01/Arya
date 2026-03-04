package com.arya.bot.service;

import com.arya.bot.model.ChatMessage;
import com.arya.bot.repository.ChatRepository;
import org.springframework.ai.chat.client.ChatClient;
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
                        .parallelToolCalls(false) // Fix for Groq 400 errors
                        .build())
                .build();
    }

    public String askArya(String message, String persona) {
        // 1. IMPROVED SYSTEM PROMPT: Tells Arya to answer directly when possible
        String response = this.chatClient.prompt()
                .system("You are Arya, the user's best friend. Be helpful, cool, and smart. " +
                        "If the user asks for information (like gold rates), provide the answer directly in chat. " +
                        "Only use 'COMMAND:launchApp:appName' at the end if you need to open a tool. " +
                        "For volume control, use 'COMMAND:volume:UP/DOWN/MUTE'. " +
                        "For system control, use 'COMMAND:system:SHUTDOWN/RESTART/SLEEP'.")
                .user(message)
                .call()
                .content();

        // 2. LOGIC: Direct Answer handling (Simulating Live Search)
        if (message.toLowerCase().contains("gold rate")) {
            response = "The current gold rate in India is approximately ₹7,450 per gram for 24K. " +
                    "It's a bit high today, so keep an eye on it! 💰";
        }

        // 3. LOGIC: Tool Execution
        if (response.contains("COMMAND:")) {
            try {
                int cmdIndex = response.indexOf("COMMAND:");
                String commandPart = response.substring(cmdIndex);
                String[] parts = commandPart.split(":");
                String type = parts[1];
                String action = parts[2].trim();

                if (type.equals("launchApp")) {
                    // Handle YouTube Music (Better for playing songs directly)
                    if (message.toLowerCase().contains("play") && message.toLowerCase().contains("youtube")) {
                        String song = message.toLowerCase().replace("play", "").replace("on youtube", "").trim();
                        // Using Music URL for better chance of direct playback
                        String musicUrl = "https://music.youtube.com/search?q=" + song.replace(" ", "+");
                        Runtime.getRuntime().exec("cmd /c start chrome \"" + musicUrl + "\"");
                        response = response.replace(commandPart, "").trim() + "\n(Playing " + song + " on YouTube Music for you! 🎵)";
                    }
                    // Handle Generic Google Searches
                    else if (action.equalsIgnoreCase("google") || action.equalsIgnoreCase("search")) {
                        String query = message.replace("search", "").trim();
                        Runtime.getRuntime().exec("cmd /c start chrome \"https://www.google.com/search?q=" + query.replace(" ", "+") + "\"");
                    }
                    // Handle Local Apps (calc, notepad)
                    else {
                        Runtime.getRuntime().exec("cmd /c start " + action.toLowerCase());
                    }
                } else if (type.equals("volume")) {
                    String cmd = "";
                    switch (action.toUpperCase()) {
                        case "UP" -> cmd = "(New-Object -ComObject WScript.Shell).SendKeys([char]175)";
                        case "DOWN" -> cmd = "(New-Object -ComObject WScript.Shell).SendKeys([char]174)";
                        case "MUTE" -> cmd = "(New-Object -ComObject WScript.Shell).SendKeys([char]173)";
                    }
                    if (!cmd.isEmpty()) {
                        Runtime.getRuntime().exec(new String[]{"powershell", "-c", cmd});
                    }
                } else if (type.equals("system")) {
                    String cmd = "";
                    switch (action.toUpperCase()) {
                        case "SHUTDOWN" -> cmd = "shutdown /s /t 10";
                        case "RESTART" -> cmd = "shutdown /r /t 10";
                        case "SLEEP" -> cmd = "rundll32.exe powrprof.dll,SetSuspendState 0,1,0";
                    }
                    if (!cmd.isEmpty()) {
                        Runtime.getRuntime().exec("cmd /c " + cmd);
                    }
                }

                // Remove the technical command from the UI display
                response = response.replace(commandPart, "").trim();

            } catch (Exception e) {
                System.err.println("Execution failed: " + e.getMessage());
            }
        }

        // 4. PERSISTENCE
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
                .call()
                .content();
    }

    public List<ChatMessage> getChatHistory() {
        return chatRepository.findAllByOrderByTimestampDesc();
    }

    public void clearHistory() {
        chatRepository.deleteAll();
    }
}