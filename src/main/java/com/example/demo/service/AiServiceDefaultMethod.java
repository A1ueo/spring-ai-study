package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AiServiceDefaultMethod {

	private ChatClient chatClient;

	public AiServiceDefaultMethod(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder
				.defaultSystem("적절한 감탄사, 웃음 등을 넣어서 친절하게 대화해주세요.")
				.defaultOptions(ChatOptions.builder()
						.temperature(0.3)
						.maxTokens(300)
						.build())
				.build();
	}

	public Flux<String> defaultMethod(String question) {
		Flux<String> response = chatClient.prompt()
				.user(question)
				.stream()
				.content();

		return response;
	}


}
