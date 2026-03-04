package com.arya.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import java.util.function.Function;

@Configuration
public class SystemTool {

    // Records for type-safe requests
    public record AppRequest(String appName) {}
    public record UrlRequest(String url) {}
    public record VolumeRequest(String action) {} // UP, DOWN, MUTE
    public record SystemRequest(String command) {} // SHUTDOWN, RESTART, SLEEP

    @Bean("launchApp")
    @Description("Opens a system application. Example: 'calc', 'notepad', 'chrome'.")
    public Function<AppRequest, String> launchApp() {
        return request -> {
            try {
                Runtime.getRuntime().exec("cmd /c start " + request.appName());
                return "Application " + request.appName() + " launched.";
            } catch (Exception e) {
                return "Failed to launch: " + e.getMessage();
            }
        };
    }

    @Bean("openBrowser")
    @Description("Opens a specific URL in the default browser. Useful for searching or playing music.")
    public Function<UrlRequest, String> openBrowser() {
        return request -> {
            try {
                String url = request.url();
                if (!url.startsWith("http")) {
                    url = "https://" + url;
                }
                Runtime.getRuntime().exec("cmd /c start " + url);
                return "Opened URL: " + url;
            } catch (Exception e) {
                return "Failed to open URL: " + e.getMessage();
            }
        };
    }

    @Bean("controlVolume")
    @Description("Controls system volume. Actions: 'UP', 'DOWN', 'MUTE'.")
    public Function<VolumeRequest, String> controlVolume() {
        return request -> {
            try {
                String cmd = "";
                switch (request.action().toUpperCase()) {
                    case "UP" -> cmd = "(New-Object -ComObject WScript.Shell).SendKeys([char]175)";
                    case "DOWN" -> cmd = "(New-Object -ComObject WScript.Shell).SendKeys([char]174)";
                    case "MUTE" -> cmd = "(New-Object -ComObject WScript.Shell).SendKeys([char]173)";
                    default -> { return "Unknown volume action. Use UP, DOWN, or MUTE."; }
                }
                Runtime.getRuntime().exec(new String[]{"powershell", "-c", cmd});
                return "Volume " + request.action() + " executed.";
            } catch (Exception e) {
                return "Failed to control volume: " + e.getMessage();
            }
        };
    }

    @Bean("systemControl")
    @Description("Performs system power actions. Commands: 'SHUTDOWN', 'RESTART', 'SLEEP'.")
    public Function<SystemRequest, String> systemControl() {
        return request -> {
            try {
                String cmd = "";
                switch (request.command().toUpperCase()) {
                    case "SHUTDOWN" -> cmd = "shutdown /s /t 10";
                    case "RESTART" -> cmd = "shutdown /r /t 10";
                    case "SLEEP" -> cmd = "rundll32.exe powrprof.dll,SetSuspendState 0,1,0";
                    default -> { return "Unknown system command. Use SHUTDOWN, RESTART, or SLEEP."; }
                }
                Runtime.getRuntime().exec("cmd /c " + cmd);
                return "System " + request.command() + " initiated.";
            } catch (Exception e) {
                return "Failed to execute system command: " + e.getMessage();
            }
        };
    }
}