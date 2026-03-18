package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ETLService {

	private ChatModel chatModel;
	private VectorStore vectorStore;

	public ETLService(ChatModel chatModel, VectorStore vectorStore) {
		this.chatModel = chatModel;
		this.vectorStore = vectorStore;
	}

	public String etlFromFile(String title, String author,
							  MultipartFile attach) throws IOException {
		List<Document> documents = extractFromFile(attach);
		if (documents == null) {
			return ".txt, .pdf, .doc. docx 파일 중에 하나를 올려주세요.";
		}
		log.info("추출된 Document 수 : {} 개", documents.size());

		for (Document doc : documents) {
			Map<String, Object> metadata = doc.getMetadata();
			metadata.putAll(Map.of(
					"title", title,
					"author", author,
					"source", attach.getOriginalFilename()
			));
		}

		documents = transform(documents);
		log.info("변환된 Document 수: {} 개", documents.size());

		vectorStore.add(documents);

		return "올린 문서를 추출-변환-적재 완료 했습니다.";
	}

	private List<Document> extractFromFile(MultipartFile attach) throws IOException {
		Resource resource = new ByteArrayResource(attach.getBytes());

		List<Document> documents = null;
		if (attach.getContentType().equals("text/plain")) {
			DocumentReader reader = new TextReader(resource);
			documents = reader.read();
		} else if (attach.getContentType().equals("application/pdf")) {
			DocumentReader reader = new PagePdfDocumentReader(resource);
			documents = reader.read();
		} else if (attach.getContentType().equals("wordprocessingml")) {
			DocumentReader reader = new TikaDocumentReader(resource);
			documents = reader.read();
		}

		return documents;
	}

	private List<Document> transform(List<Document> documents) {
		List<Document> transformedDocuments = null;

		TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
		transformedDocuments = tokenTextSplitter.apply(documents);

		KeywordMetadataEnricher keywordMetadataEnricher =
				new KeywordMetadataEnricher(chatModel, 5);
		transformedDocuments = keywordMetadataEnricher.apply(transformedDocuments);

		return transformedDocuments;
	}
}
