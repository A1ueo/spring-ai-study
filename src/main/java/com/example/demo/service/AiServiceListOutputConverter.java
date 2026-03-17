package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AiServiceListOutputConverter {

	private ChatClient chatClient;

	public AiServiceListOutputConverter(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}

	public List<String> listOutputConverterLowLevel(String city) {
		ListOutputConverter converter = new ListOutputConverter();

		PromptTemplate promptTemplate = PromptTemplate.builder()
				.template("{city}에서 유명한 호텔 목록 5개를 출력하세요. {format}")
				.build();

		Prompt prompt = promptTemplate.create(
				Map.of("city", city, "format", converter.getFormat())
		);

		String commaSeparatedString = chatClient.prompt(prompt)
				.call()
				.content();

		log.info(converter.getFormat());
		log.info(commaSeparatedString);
		List<String> hotelList = converter.convert(commaSeparatedString);

		return hotelList;
	}

	public List<String> listOutputConverterHighLevel(String city) {
		List<String> hotelList = chatClient.prompt()
				.user("%s에서 유명한 호텔 목록 5개를 출력하세요.".formatted(city))
				.call()
				.entity(new ListOutputConverter());

		return hotelList;
	}
}
