package com.example.demo.controller;

import com.example.demo.dto.Hotel;
import com.example.demo.service.AiServiceBeanOutputConverter;
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
public class AiControllerBeanOutputConverter {

	@Autowired
	private AiServiceBeanOutputConverter aiService;

	@PostMapping(
			value = "/bean-output-converter",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Hotel beanOutputConverter(@RequestParam("city") String city) {
//		Hotel hotel = aiService.beanOutputConverterLowLevel(city);
		Hotel hotel = aiService.beanOutputConverterHighLevel(city);
		return hotel;
	}
}
