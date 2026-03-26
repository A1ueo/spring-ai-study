package com.example.demo.filesystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileSystemService {

	private ChatClient chatClient;

	@Autowired
	private FileSystemTools fileSystemTools;

	public FileSystemService(ChatModel chatModel, ChatMemory chatMemory) {
		this.chatClient = ChatClient.builder(chatModel)
				.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
				.defaultSystem("""
						파일, 디렉토리 관련 질문은 반드시 도구를 사용하세요.""")
				.build();
	}

	public String chat(String question, String conversationId) {
		String answer = this.chatClient.prompt()
				.user(question)
				.advisors(advisorSpec -> advisorSpec.param(
						ChatMemory.CONVERSATION_ID, conversationId
				))
				.tools(fileSystemTools)
				.call()
				.content();

		return answer;
	}
}
