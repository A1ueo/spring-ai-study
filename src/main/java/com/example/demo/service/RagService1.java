package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;


import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class RagService1 {

	private ChatClient chatClient;
	@Autowired
	private VectorStore vectorStore;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public RagService1(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder
				.defaultAdvisors(
						new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
				).build();
	}

	public void clearVectorStore() {
		jdbcTemplate.update("TRUNCATE TABLE vector_store");
	}

	public void ragEtl(MultipartFile attach, String source, int chunkSize,
					   int minChunkSizeChars) throws IOException {
		Resource resource = new ByteArrayResource(attach.getBytes());
		DocumentReader reader = new PagePdfDocumentReader(resource);
		List<Document> documents = reader.read();

		for (Document doc : documents) {
			doc.getMetadata().put("source", source);
		}

		DocumentTransformer transformer = new TokenTextSplitter(
				chunkSize, minChunkSizeChars, 5, 10000, true
		);

		List<Document> transformedDocuments = transformer.apply(documents);

		vectorStore.add(transformedDocuments);
	}

	public String ragChat(String question, double score, String source) {
		SearchRequest.Builder searchRequestBuilder = SearchRequest.builder()
				.similarityThreshold(score)
				.topK(5);
		if(StringUtils.hasText(source)) {
			searchRequestBuilder.filterExpression("source == '%s'".formatted(source));
		}
		SearchRequest searchRequest = searchRequestBuilder.build();

		QuestionAnswerAdvisor questionAnswerAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
				.searchRequest(searchRequest)
				.build();

		String answer = this.chatClient.prompt()
				.user(question)
				.advisors(questionAnswerAdvisor)
				.call()
				.content();

		return answer;
	}
}
