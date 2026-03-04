package com.arya.bot;

import com.arya.bot.model.ChatMessage;
import com.arya.bot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3001")
public class AryaController {

	private final ChatService chatService;

	// Standard Chat Endpoint
	@GetMapping("/ask")
	public String ask(@RequestParam String message) {
		return chatService.askArya(message, "Assistant");
	}

	// NEW: Vision Endpoint (Handles Image Upload)
	@PostMapping("/vision")
	public String askWithVision(
			@RequestParam("message") String message,
			@RequestParam("file") MultipartFile file) {
		try {
			// Convert the uploaded file into a Spring Resource for the ChatService
			InputStreamResource resource = new InputStreamResource(file.getInputStream());
			return chatService.askAryaWithVision(message, "VisionAssistant", resource);
		} catch (Exception e) {
			return "Error processing image: " + e.getMessage();
		}
	}

	@GetMapping("/history") // This was missing, causing the 404
	public List<ChatMessage> getHistory() {
		return chatService.getChatHistory();
	}


	@DeleteMapping("/history")
	@CrossOrigin(origins = "http://localhost:3001")
	public ResponseEntity<String> clearHistory() {
		chatService.clearHistory();
		return ResponseEntity.ok("History cleared successfully");
	}
}