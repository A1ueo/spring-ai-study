package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AiServiceRoleAssignmentPrompt {

	private ChatClient chatClient;

	public AiServiceRoleAssignmentPrompt(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}

	public Flux<String> roleAssignment(String requirements) {
		Flux<String> travelSuggestions = chatClient.prompt()
				.system("""
						당신이 여행 가이드 역할을 해주었으면 좋겠습니다.
						아래 요청사항에서 위치를 알려주면, 근처에 있는 3곳을 제안해주고,
						이유를 달아주세요. 경우에 따라서 방문하고 싶은 장소 유형을 제공할 수도 있습니다.
						출력 형식은 <ul> 태그이고, 장소는 굵게 표시해주세요.""")
				.user("요청사항: %s".formatted(requirements))
				.options(ChatOptions.builder()
						.temperature(1.0)
						.maxTokens(1000)
						.build())
				.stream()
				.content();

		return travelSuggestions;
	}
}
