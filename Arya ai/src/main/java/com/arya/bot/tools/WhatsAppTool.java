package com.arya.bot.tools;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
@Configuration
public class WhatsAppTool {

    // Record must be here
    public record WhatsAppRequest(String phoneNumber, String message) {}

    @Bean
    @Description("Send a WhatsApp message to a specific phone number")
    public Function<WhatsAppRequest, String> sendWhatsAppMessage() {
        return request -> {
            try {
                String url = "https://web.whatsapp.com/send?phone=" + request.phoneNumber() +
                        "&text=" + URLEncoder.encode(request.message(), StandardCharsets.UTF_8);
                Runtime.getRuntime().exec("cmd /c start " + url);
                return "Arya: Opening WhatsApp Web...";
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        };
    }
}