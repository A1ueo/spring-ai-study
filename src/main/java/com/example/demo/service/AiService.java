package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AiService {

	private ChatClient chatClient;
	private OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
	private OpenAiAudioSpeechModel openAiAudioSpeechModel;

	public AiService(ChatClient.Builder chatCliBuilder,
					 OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel,
					 OpenAiAudioSpeechModel openAiAudioSpeechModel) {
		this.chatClient = chatCliBuilder.build();
		this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
		this.openAiAudioSpeechModel = openAiAudioSpeechModel;
	}

	public String stt(String fileName, byte[] bytes) {
		Resource audioResource = new ByteArrayResource(bytes) {
			@Override
			public String getFilename() {
				return fileName;
			}
		};

		OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
				.model("whisper-1")
				.language("ko")
				.build();

		AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(audioResource, options);

		AudioTranscriptionResponse response = openAiAudioTranscriptionModel.call(prompt);
		String text = response.getResult().getOutput();

		return text;
	}

	public byte[] tts(String text) {
		OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
				.model("gpt-4o-mini-tts")
				.voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
				.responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
				.speed(1.3f)
				.build();

		SpeechPrompt prompt = new SpeechPrompt(text, options);

		SpeechResponse response = openAiAudioSpeechModel.call(prompt);
		byte[] bytes = response.getResult().getOutput();

		return bytes;
	}

//	public Map<String, String> chatText(String question) {}
}
