package com.example.demo.service;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AiService {

    private final ChatModel chatModel;

    AiService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String generateText(String question) {
        SystemMessage systemMessage = SystemMessage.builder()
                .text("Answer in Korean language.")
                .build();

        UserMessage userMessage = UserMessage.builder()
                .text(question)
                .build();

        ChatOptions chatOptions = ChatOptions.builder()
                .model("llama3.2")
                .temperature(0.3)
                .maxTokens(1000)
                .build();

        Prompt prompt = Prompt.builder()
                .messages(systemMessage, userMessage)
                .chatOptions(chatOptions)
                .build();

        ChatResponse chatResponse = chatModel.call(prompt);
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        String answer = assistantMessage.getText();

        return answer;
    }
}
