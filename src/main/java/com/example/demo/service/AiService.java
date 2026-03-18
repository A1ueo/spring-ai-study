package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiService {

	private ChatClient chatClient;

	public AiService(ChatClient.Builder chatClientBuilder, JdbcTemplate jdbcTemplate,
					 EmbeddingModel embeddingModel) {
		VectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, embeddingModel)
				.initializeSchema(false)
				.schemaName("public")
				.vectorTableName("chat_memory_vector_store")
				.build();

		this.chatClient = chatClientBuilder
				.defaultAdvisors(
						VectorStoreChatMemoryAdvisor.builder(vectorStore)
								.defaultTopK(5)
								.build(),
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
