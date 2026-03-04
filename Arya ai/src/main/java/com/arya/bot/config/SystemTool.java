package com.arya.bot.config;// Make sure this matches your folder structure

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import java.util.function.Function;

@Configuration
public class SystemTool {

    // Input data for the tool
    public record AppRequest(String appName) {}

    @Bean("launchApp")
    @Description("Opens a system application. Pass 'calc' for calculator.")
    public Function<String, String> launchApp() {
        return appName -> {
            try {
                Runtime.getRuntime().exec("cmd /c start " + appName);
                return "Application " + appName + " launched.";
            } catch (Exception e) {
                return "Failed to launch: " + e.getMessage();
            }
        };
    }
}