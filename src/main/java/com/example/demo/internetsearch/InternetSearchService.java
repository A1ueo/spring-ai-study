package com.example.demo.internetsearch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InternetSearchService {

	private ChatClient chatClient;

	@Autowired
	private InternetSearchTools internetSearchTools;

	public InternetSearchService(ChatModel chatModel) {
		this.chatClient = ChatClient.builder(chatModel).build();
	}

	public String chat(String question) {
		String answer = this.chatClient.prompt()
				.user(question)
				.tools(internetSearchTools)
				.call()
				.content();
		return answer;
	}
}
