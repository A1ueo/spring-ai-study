package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.EmbeddingResponseMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AiService {

	@Autowired
	private EmbeddingModel embeddingModel;

	public void textEmbedding1(String question) {
		EmbeddingResponse response = embeddingModel.embedForResponse(List.of(question));

		EmbeddingResponseMetadata metadata = response.getMetadata();
		log.info("모델 이름: {}", metadata.getModel());
		log.info("모델의 임베딩 차원: {}", embeddingModel.dimensions());

		Embedding embedding = response.getResults().get(0);
		log.info("벡터 차원: {}", embedding.getOutput().length);
		log.info("벡터: {}", embedding.getOutput());
	}

	public void textEmbedding2(String question) {
		float[] vector = embeddingModel.embed(question);
		log.info("벡터 차원: {}", vector.length);
		log.info("벡터: {}", vector);
	}
}
