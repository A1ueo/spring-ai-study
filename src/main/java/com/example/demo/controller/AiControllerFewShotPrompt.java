package com.example.demo.controller;

import com.example.demo.service.AiServiceFewShotPrompt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@Slf4j
public class AiControllerFewShotPrompt {

	@Autowired
	private AiServiceFewShotPrompt aiService;

	@PostMapping(
			value = "/few-shot-prompt",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public String fewShotPrompt(@RequestParam("order") String order) {
		String json = aiService.fewShotPrompt(order);

		return json;
	}
}
