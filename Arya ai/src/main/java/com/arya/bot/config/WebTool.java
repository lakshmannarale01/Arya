package com.arya.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.client.RestTemplate;
import java.util.function.Function;

@Configuration
public class WebTool {

    public record SearchRequest(String query) {}

    @Bean
    @Description("Search the internet for live information, news, or technical documentation.")
    public Function<SearchRequest, String> webSearch() {
        return request -> {
            // In a real scenario, you'd call a Search API here.
            // For now, let's simulate the connection.
            return "Arya: I searched the web for '" + request.query() + "' and found relevant data.";
        };
    }
}