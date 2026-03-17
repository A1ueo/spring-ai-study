package com.example.demo.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

import javax.validation.constraints.Max;

@Slf4j
public class MaxCharLengthAdvisor implements CallAdvisor {

	public static final String MAX_CHAR_LENGTH = "maxCharLength";
	private int maxCharLength = 300;
	private int order;

	public MaxCharLengthAdvisor(int order) {
		this.order = order;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	@Override
	public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
		ChatClientRequest mutatedRequest = augmentPrompt(request);
		ChatClientResponse response = chain.nextCall(mutatedRequest);

		return response;
	}

	private ChatClientRequest augmentPrompt(ChatClientRequest request) {
		String userText = this.maxCharLength + "자 이내로 답변해주세요.";
		Integer maxCharLength = (Integer) request.context().get(MAX_CHAR_LENGTH);
		if (maxCharLength != null) {
			userText = maxCharLength + "자 이내로 답변해주세요.";
		}
		String finalUserText = userText;

		Prompt originalPrompt = request.prompt();
		Prompt augmentedPrompt = originalPrompt.augmentUserMessage(
				userMessage -> UserMessage.builder()
						.text(userMessage.getText() + " " + finalUserText)
						.build()
		);

		ChatClientRequest mutatedRequest = request.mutate()
				.prompt(augmentedPrompt)
				.build();

		return mutatedRequest;
	}
}
