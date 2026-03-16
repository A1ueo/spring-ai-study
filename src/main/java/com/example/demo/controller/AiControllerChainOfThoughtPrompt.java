package com.example.demo.controller;

import com.example.demo.service.AiServiceChainOfThoughtPrompt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.Ansi8BitColor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/ai")
@Slf4j
public class AiControllerChainOfThoughtPrompt {

	@Autowired
	private AiServiceChainOfThoughtPrompt  aiService;

	@PostMapping(
			value = "/chain-of-thought",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_NDJSON_VALUE
	)
	public Flux<String> chainOfThought(String question) {
		Flux<String> answer = aiService.chainOfThought(question);

		return answer;
	}
}
