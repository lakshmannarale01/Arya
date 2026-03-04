package com.arya.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.function.Function;
import java.io.File;
import java.nio.file.Files;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

@Configuration
public class AdvancedTools {

    private final JdbcTemplate jdbcTemplate;

    public AdvancedTools(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public record EmptyRequest() {}

    // --- 1. DATABASE SEARCH TOOL ---
    public record SqlRequest(String query) {}
    @Bean
    @Description("Execute a SELECT SQL query on the arya_db database to fetch user data or logs.")
    public Function<SqlRequest, String> searchDatabase() {
        return request -> {
            try {
                var result = jdbcTemplate.queryForList(request.query());
                return "Arya found: " + result.toString();
            } catch (Exception e) {
                return "Database Error: " + e.getMessage();
            }
        };
    }

    // --- 2. SYSTEM INFO TOOL ---
    @Bean
    @Description("Get current system CPU usage and available memory info.")
    public Function<EmptyRequest, String> getSystemStats() { // Changed String to EmptyRequest
        return request -> {
            OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            double cpu = osBean.getCpuLoad() * 100;
            long memory = osBean.getFreeMemorySize() / (1024 * 1024);
            return String.format("System Status: CPU Usage: %.2f%%, Free RAM: %d MB", cpu, memory);
        };
    }

    // --- 3. FILE CREATOR TOOL ---
    public record FileRequest(String fileName, String content) {}
    @Bean
    @Description("Create a new text or java file on the user's desktop with specific content.")
    public Function<FileRequest, String> createFile() {
        return request -> {
            try {
                String desktopPath = System.getProperty("user.home") + "/Desktop/" + request.fileName();
                Files.writeString(new File(desktopPath).toPath(), request.content());
                return "Arya: I created " + request.fileName() + " on your Desktop.";
            } catch (Exception e) {
                return "File Error: " + e.getMessage();
            }
        };
    }
}