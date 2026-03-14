package com.example.demo.controller;

import com.example.demo.service.AiServiceByClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.AiService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/ai")
@Slf4j
public class AiController {

	//	private final AiService aiService;
	private final AiServiceByClient aiService;

	public AiController(AiServiceByClient aiService) {
		this.aiService = aiService;
	}

	@PostMapping(value = "/chat-model",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
	)
	public String postMethodName(@RequestParam("question") String question) {
		String answerText = aiService.generateText(question);
		return answerText;
	}

	@PostMapping(
			value = "/chat-model-stream",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_NDJSON_VALUE
	)
	public Flux<String> chatModelStream(@RequestParam("question") String question) {
		Flux<String> answerStreamText = aiService.generateStreamText(question);
		return answerStreamText;
	}

}
