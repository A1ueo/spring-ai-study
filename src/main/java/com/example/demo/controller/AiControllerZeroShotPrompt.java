package com.example.demo.controller;

import com.example.demo.service.AiServiceZeroShotPrompt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@Slf4j
public class AiControllerZeroShotPrompt {

	@Autowired
	private AiServiceZeroShotPrompt aiService;

	@PostMapping(
			value = "/zero-shot-prompt",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
	)
	public String zeroShotPrompt(String review) {
		String reviewSentiment = aiService.zeroShotPrompt(review);

		return reviewSentiment;
	}
}
