package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.cassandra.CassandraChatMemoryRepository;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiService {

	private ChatClient chatClient;

	public AiService(ChatClient.Builder chatClientBuilder, CassandraChatMemoryRepository chatMemoryRepository) {
		ChatMemory chatMemory = MessageWindowChatMemory.builder()
				.chatMemoryRepository(chatMemoryRepository)
				.maxMessages(20)
				.build();

		this.chatClient = chatClientBuilder
				.defaultAdvisors(
						PromptChatMemoryAdvisor.builder(chatMemory).build(),
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
