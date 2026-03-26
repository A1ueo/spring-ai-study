package com.example.demo.exceptionhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("recommendMovieController2")
@RequestMapping("/ai")
@Slf4j
public class RecommendMovieController {
	// ##### 필드 #####
	@Autowired
	@Qualifier("recommendMovieService2")
	private RecommendMovieService recommendMovieService;

	// ##### 요청 매핑 메소드 #####
	@PostMapping(
			value = "/exception-handling",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
	)
	public String recommendMovieTools(@RequestParam("question") String question) {
		try {
			String answer = recommendMovieService.chat(question);
			return answer;
		} catch (Exception e) {
			return "[어플리케이션] 질문을 처리할 수 없습니다.";
		}
	}
}
