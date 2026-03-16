package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AiServiceZeroShotPrompt {

	private ChatClient chatClient;
	private PromptTemplate promptTemplate;

	public AiServiceZeroShotPrompt(ChatClient.Builder clientBuilder) {
		this.chatClient = clientBuilder
				.defaultOptions(ChatOptions.builder()
						.temperature(0.0)
						.maxTokens(4)
						.build())
				.build();

		this.promptTemplate = PromptTemplate.builder()
				.template("""
						영화 리뷰를 1부터 5중에 점수를 매겨주세요. 
						레이블만 반환하세요.
						리뷰: {review}""").build();
	}

	public String zeroShotPrompt(String review) {
		String sentiment = chatClient.prompt()
				.user(promptTemplate.render(Map.of("review", review)))
				.call()
				.content();

		return sentiment;
	}
}
