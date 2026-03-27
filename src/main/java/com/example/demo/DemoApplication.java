package com.example.demo;

import com.example.demo.tool.DateTimeTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	private DateTimeTools dateTimeTools;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public ToolCallbackProvider getToolCallbackProvider() {
		return MethodToolCallbackProvider.builder()
				.toolObjects(dateTimeTools)
				.build();
	}

}
