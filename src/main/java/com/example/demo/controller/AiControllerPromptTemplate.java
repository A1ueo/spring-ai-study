package com.example.demo.controller;

import com.example.demo.service.AiServicePromptTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@Slf4j
public class AiControllerPromptTemplate {

	private final AiServicePromptTemplate aiServicePromptTemplate;

	AiControllerPromptTemplate(AiServicePromptTemplate aiServicePromptTemplate) {
		this.aiServicePromptTemplate = aiServicePromptTemplate;
	}

	@PostMapping(
			value = "/prompt-template",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_NDJSON_VALUE
	)
	public Flux<String> promptTemplate(
			@RequestParam("statement") String statement,
			@RequestParam("language") String language
	) {
		Flux<String> response = aiServicePromptTemplate.promptTemplate3(statement, language);

		return response;
	}
}
