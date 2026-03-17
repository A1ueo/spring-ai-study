package com.example.demo.controller;

import com.example.demo.service.AiServiceListOutputConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ai")
@Slf4j
public class AiControllerListOutputConverter {

	@Autowired
	private AiServiceListOutputConverter aiService;

	@PostMapping(
			value = "/list-output-converter",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public List<String> listOutputConverter(String city) {
		List<String> hotelList = aiService.listOutputConverterLowLevel(city);
//		List<String> hotelList = aiService.listOutputConverterHighLevel(city);

		return hotelList;
	}
}
