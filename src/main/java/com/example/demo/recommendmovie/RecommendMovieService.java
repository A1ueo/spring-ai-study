package com.example.demo.recommendmovie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RecommendMovieService {

	private ChatClient chatClient;

	@Autowired
	private RecommendMovieTools recommendMovieTools;

	public RecommendMovieService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}

	public String chat(String question) {
		String answer = chatClient.prompt()
				.user(question)
				.tools(recommendMovieTools)
				.call()
				.content();

		return answer;
	}
}
