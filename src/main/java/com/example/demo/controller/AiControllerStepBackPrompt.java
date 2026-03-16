package com.example.demo.controller;

import com.example.demo.service.AiServiceStepBackPrompt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@Slf4j
public class AiControllerStepBackPrompt {

	@Autowired
	private AiServiceStepBackPrompt aiService;

	@PostMapping(
			value = "/step-back-prompt",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
	)
	public String stepBackPrompt(String question) throws Exception{
		String answer = aiService.stepBackPrompt(question);

		return answer;
	}
}
