package com.arya.bot;

import com.arya.bot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AryaController {


	private final ChatService chatService;

	@GetMapping("/ask")
	public String ask(
			@RequestParam String message,
			@RequestParam(defaultValue = "best friend") String persona) {
		return chatService.askArya(message, persona);
	}
}