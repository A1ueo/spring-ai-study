package com.example.demo.service;

import com.example.demo.advisor.AdvisorA;
import com.example.demo.advisor.AdvisorB;
import com.example.demo.advisor.AdvisorC;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AiService1 {

	private ChatClient chatClient;

	public AiService1(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder
				.defaultAdvisors(new AdvisorA(),
						new AdvisorB())
				.build();
	}

	public String advisorChain1(String question) {
		String response = chatClient.prompt()
				.advisors(new AdvisorC())
				.user(question)
				.call()
				.content();

		return response;
	}

	public Flux<String> advisorChain2(String question) {
		Flux<String> response = chatClient.prompt()
				.advisors(new AdvisorC())
				.stream()
				.content();

		return response;
	}
}
