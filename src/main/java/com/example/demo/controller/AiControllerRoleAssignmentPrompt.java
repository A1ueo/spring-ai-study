package com.example.demo.controller;

import com.example.demo.service.AiServiceRoleAssignmentPrompt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/ai")
@Slf4j
public class AiControllerRoleAssignmentPrompt {

	@Autowired
	private AiServiceRoleAssignmentPrompt aiService;

	@PostMapping(
			value = "/role-assignment",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_NDJSON_VALUE
	)
	public Flux<String> roleAssignment(String requirements) {
		Flux<String> travelSuggestions = aiService.roleAssignment(requirements);

		return travelSuggestions;
	}
}
