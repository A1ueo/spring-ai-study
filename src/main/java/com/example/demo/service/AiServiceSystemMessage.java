package com.example.demo.service;

import com.example.demo.dto.ReviewClassification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiServiceSystemMessage {

	private ChatClient chatClient;

	public AiServiceSystemMessage(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}

	public ReviewClassification classifyReview(String review) {
		ReviewClassification reviewClassification = chatClient.prompt()
				.system("""
						영화 리뷰를 [POSITIVE, NEUTRAL, NEGATIVE] 중에서 하나로 분류하고,
						유요한 JSON을 반환하세요.""")
				.user(review)
				.options(ChatOptions.builder()
						.temperature(0.0)
						.build())
				.call()
				.entity(ReviewClassification.class);

		return reviewClassification;
	}
}
