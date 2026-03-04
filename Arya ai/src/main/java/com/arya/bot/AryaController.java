package com.arya.bot;

import com.arya.bot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3001")
public class AryaController {

	private final ChatService chatService;

	// Standard Chat Endpoint
	@GetMapping("/ask")
	public String ask(@RequestParam String message, @RequestParam(defaultValue = "helper") String persona) {
		return chatService.askArya(message, persona);
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
}