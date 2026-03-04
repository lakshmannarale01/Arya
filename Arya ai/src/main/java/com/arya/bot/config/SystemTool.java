package com.arya.bot.config;// Make sure this matches your folder structure

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import java.util.function.Function;

@Configuration
public class SystemTool {

    // Input data for the tool
    public record AppRequest(String appName) {}

    @Bean
    @Description("Launch a Windows application like 'notepad', 'chrome', or 'calc'")
    public Function<AppRequest, String> launchApp() {
        return request -> {
            try {
                Runtime.getRuntime().exec("cmd /c start " + request.appName());
                return "Successfully opened " + request.appName();
            } catch (Exception e) {
                return "Failed to open app: " + e.getMessage();
            }
        };
    }
}