package com.example.demo.controller;

import com.example.demo.service.AiServiceDefaultMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/ai")
@Slf4j
public class AiControllerDefaultMethod {

	@Autowired
	private AiServiceDefaultMethod aiService;

	@PostMapping("/default-method")
	public Flux<String> defaultMethod(String question) {
		Flux<String> response = aiService.defaultMethod(question);

		return response;
	}
}
