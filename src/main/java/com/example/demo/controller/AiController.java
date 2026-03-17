package com.example.demo.controller;

import com.example.demo.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

	@PostMapping(
			value = "/search-document-1",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
	)
	public String searchDocument1(@RequestParam("question") String question) {
		List<Document> documents = aiService.searchDocument1(question);

		String text = "";
		for (Document document : documents) {
			text += "<div class='mb-2'>";
			text += "  <span class='me-2'>유사도 점수: %f,</span>".formatted(document.getScore());
			text += "  <span>%s(%s)</span>".formatted(document.getText(),
					document.getMetadata().get("year"));
			text += "</div>";
		}
		return text;
	}

	@PostMapping(
			value = "/search-document-2",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
	)
	public String searchDocument2(@RequestParam("question") String question) {
		List<Document> documents = aiService.searchDocument3(question);

		String text = "";
		for (Document document : documents) {
			text += "<div class='mb-2'>";
			text += "  <span class='me-2'>유사도 점수: %f,</span>".formatted(document.getScore());
			text += "  <span>%s(%s)</span>".formatted(document.getText(),
					document.getMetadata().get("year"));
			text += "</div>";
		}
		return text;
	}
}
