package com.arya.bot.tools;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import java.util.function.Function;

@Configuration
public class WhatsAppTool {

    // This is the "Tool Definition" that Arya reads
    @Bean
    @Description("Sends a message to a specific contact on WhatsApp")
    public Function<WhatsAppRequest, WhatsAppResponse> sendWhatsAppMessage() {
        return request -> {
            // This is where the actual automation happens
            System.out.println("ARYA ACTION: Sending '" + request.message() + "' to " + request.recipient());

            // For now, we simulate success. Later we plug in Twilio or Selenium.
            return new WhatsAppResponse("Successfully sent message to " + request.recipient());
        };
    }

    // Data structures for the tool
    public record WhatsAppRequest(String recipient, String message) {}
    public record WhatsAppResponse(String status) {}
}