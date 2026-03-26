package com.example.demo.boombarrier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CarCheckTools {

	private List<String> carNumbers = List.of(
			"23가4567", "234부8372", "345가6789"
	);

	@Tool(description = "인식된 차량 번호가 등록되어 있는지 확인합니다.")
	public String checkCarNumber(@ToolParam(description = "차량 번호") String carNumber) {
		carNumber = carNumber.replaceAll("\\s+", "");
		log.info("LLM이 인식한 차량 번호: {}", carNumber);

		boolean result = carNumbers.contains(carNumber);
		return result ? "등록번호" : "미등록번호";
	}
}
