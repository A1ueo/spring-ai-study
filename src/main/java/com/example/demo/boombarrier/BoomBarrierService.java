package com.example.demo.boombarrier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

@Service
@Slf4j
public class BoomBarrierService {

	private ChatClient chatClient;

	@Autowired
	private CarCheckTools carCheckTools;

	@Autowired
	private BoomBarrierTools boomBarrierTools;

	public BoomBarrierService(ChatModel chatModel) {
		this.chatClient = ChatClient.builder(chatModel).build();
	}

	public String chat(String contentType, byte[] bytes) {
		Media media = Media.builder()
				.mimeType(MimeType.valueOf(contentType))
				.data(new ByteArrayResource(bytes))
				.build();

		UserMessage userMessage = UserMessage.builder()
				.text("""
						다음 단계별로 처리해 주세요.
						1단계: 이미지에서 '(숫자 2개~3개)-(한글 1자)-(숫자 4개)'로 구성된 차량 번호를 인식하세요. 예: 78라1234, 567바2558
						2단계: 인식된 차량 번호에서 끝에서부터 5번째 문자가 한글 완성형 음절이 아닐 경우에는 다시 1단계로 돌아가세요.
						3단계: 1단계에서 인식된 차량 번호가 등록된 차량 번호인지 도구로 확인을 하세요.
						4단계: 3단계의 결과가 "미등록번호"라면 도구로 차단기를 내리고, "등록번호"라면 도구로 차단기를 올리세요.
						
						최종 답변은 차단기 내림 또는 차단기 올림으로 하고 추가 설명은 하지마세요.""")
				.media(media)
				.build();

		String answer = chatClient.prompt()
				.messages(userMessage)
				.tools(carCheckTools, boomBarrierTools)
				.call()
				.content();

		return answer;
	}
}
