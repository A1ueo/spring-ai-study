package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiService {

	private ChatClient chatClient;

	public AiService(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
		this.chatClient = chatClientBuilder
				.defaultAdvisors(
						MessageChatMemoryAdvisor.builder(chatMemory).build(),
						new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
				)
				.build();

	}

	public String chat(String userText, String conversationId) {
		String answer = chatClient.prompt()
				.user(userText)
				.advisors(advisorSpec -> advisorSpec.param(
						ChatMemory.CONVERSATION_ID, conversationId
				))
				.call()
				.content();

		return answer;
	}
}
