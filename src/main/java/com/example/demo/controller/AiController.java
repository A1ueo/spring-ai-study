package com.example.demo.controller;

import com.example.demo.service.AiService;
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
public class AiController {

	@Autowired
	private AiService aiService;

	@PostMapping(
			value = "/text-embedding",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
	)
	public String textEmbedding(@RequestParam("question") String question) {
		aiService.textEmbedding2(question);
		return "서버 터미널(콘솔) 출력을 확인하세요.";
	}

	@PostMapping(
			value = "/add-document",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
	)
	public String addDocument(@RequestParam("question") String question) {
		aiService.addDocument();
		return "벡터 저장소에 Document들이 저장되었습니다.";
	}
}
