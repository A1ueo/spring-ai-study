package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.CompressionQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class RagService2 {

	private ChatClient chatClient;
	@Autowired
	private ChatModel chatModel;
	@Autowired
	private VectorStore vectorStore;
	@Autowired
	private ChatMemory chatMemory;

	public RagService2(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder
				.defaultAdvisors(
						new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
				).build();
	}

	private VectorStoreDocumentRetriever createVectorStoreDocumentRetriever(
			double score, String source) {
		VectorStoreDocumentRetriever vectorStoreDocumentRetriever =
				VectorStoreDocumentRetriever.builder()
						.vectorStore(vectorStore)
						.similarityThreshold(score)
						.topK(5)
						.filterExpression(() -> {
							FilterExpressionBuilder builder = new FilterExpressionBuilder();
							if (StringUtils.hasText(source)) {
								return builder.eq("source", source).build();
							} else {
								return null;
							}
						}).build();
		return vectorStoreDocumentRetriever;
	}

	private CompressionQueryTransformer createCompressionQueryTransformer() {
		ChatClient.Builder chatClientBuilder = ChatClient.builder(chatModel)
				.defaultAdvisors(
						new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
				);

		CompressionQueryTransformer compressionQueryTransformer =
				CompressionQueryTransformer.builder()
						.chatClientBuilder(chatClientBuilder)
						.build();

		return compressionQueryTransformer;
	}

	public String chatWithCompression(String question, double score, String source,
									  String conversationId) {
		RetrievalAugmentationAdvisor retrievalAugmentationAdvisor =
				RetrievalAugmentationAdvisor.builder()
						.queryTransformers(createCompressionQueryTransformer())
						.documentRetriever(createVectorStoreDocumentRetriever(score, source))
						.build();

		String answer = this.chatClient.prompt()
				.user(question)
				.advisors(
						MessageChatMemoryAdvisor.builder(chatMemory).build(),
						retrievalAugmentationAdvisor
				)
				.advisors(advisorSpec -> advisorSpec.param(
						ChatMemory.CONVERSATION_ID, conversationId
				))
				.call()
				.content();

		return answer;
	}

	private RewriteQueryTransformer createRewriteQueryTransformer() {
		ChatClient.Builder chatClientBuilder = ChatClient.builder(chatModel)
				.defaultAdvisors(
						new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
				);

		RewriteQueryTransformer rewriteQueryTransformer =
				RewriteQueryTransformer.builder()
						.chatClientBuilder(chatClientBuilder)
						.build();

		return rewriteQueryTransformer;
	}

	public String chatWithRewriteQuery(String question, double score, String source) {
		RetrievalAugmentationAdvisor retrievalAugmentationAdvisor =
				RetrievalAugmentationAdvisor.builder()
						.queryTransformers(createRewriteQueryTransformer())
						.documentRetriever(createVectorStoreDocumentRetriever(score, source))
						.build();

		String answer = this.chatClient.prompt()
				.user(question)
				.advisors(retrievalAugmentationAdvisor)
				.call()
				.content();

		return answer;
	}
}
