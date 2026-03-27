package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImageMessage;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AiService {

	private ChatClient chatClient;

	@Autowired
	private ImageModel imageModel;

	public AiService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder
				.defaultAdvisors(
						new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
				)
				.build();
	}

	public Flux<String> imageAnalysis(String question, String contentType, byte[] bytes) {
		SystemMessage systemMessage = SystemMessage.builder()
				.text("""
						당신은 이미지 분석 전문가입니다.
						사용자의 질문에 맞게 이미지를 분석하고 답변을 한국어로 하세요.""")
				.build();

		Media media = Media.builder()
				.mimeType(MimeType.valueOf(contentType))
				.data(new ByteArrayResource(bytes))
				.build();

		UserMessage userMessage = UserMessage.builder()
				.text(question)
				.media(media)
				.build();

		Flux<String> flux = chatClient.prompt()
				.messages(systemMessage, userMessage)
				.stream()
				.content();
		return flux;
	}

	private String koToEn(String text) {
		String question = """
				당신은 번역가입니다. 아래 한글 문장을 영어 문장으로 번역해주세요.
				%s""".formatted(text);

		String englishDescription = chatClient.prompt()
				.user(question)
				.call()
				.content();

		return englishDescription;
	}

	public String generateImage(String description) {
		String englishDescription = koToEn(description);

		ImageMessage imageMessage = new ImageMessage(englishDescription);

		OpenAiImageOptions imageOptions = OpenAiImageOptions.builder()
//				.model("gpt-image-1")
//				.quality("low")
//				.width(1536)
				.model("dall-e-2")
				.responseFormat("b64_json")
				.width(1024)
				.height(1024)
				.N(1)
				.build();

		ImagePrompt imagePrompt = new ImagePrompt(imageMessage, imageOptions);
		ImageResponse imageResponse = imageModel.call(imagePrompt);

		String b64Json = imageResponse.getResult().getOutput().getB64Json();
		return b64Json;
	}
}
