package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AiServiceMultiMessages {

	private ChatClient chatClient;

	public AiServiceMultiMessages(ChatClient.Builder chatClientBuilder) {
		chatClient = chatClientBuilder.build();
	}

	public String multiMessages(String question, List<Message> chatMemory) {
		SystemMessage systemMessage = SystemMessage.builder()
				.text("""
						당신은 AI 비서입니다.
						제공되는 지난 대화 내용을 보고 우선적으로 답변해주세요.""")
				.build();

		if(chatMemory.isEmpty()) {
			chatMemory.add(systemMessage);
		}

		log.info(chatMemory.toString());

		ChatResponse chatResponse = chatClient.prompt()
				.messages(chatMemory)
				.user(question)
				.call().chatResponse();

		UserMessage userMessage = UserMessage.builder()
				.text(question)
				.build();
		chatMemory.add(userMessage);

		AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
		chatMemory.add(assistantMessage);

		String text = assistantMessage.getText();

		return text;

	}
}
