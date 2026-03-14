package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.AiService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/ai")
@Slf4j
public class AiController {

    private final AiService aiService;

    AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping(value = "/chat-model",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String postMethodName(@RequestParam("question") String question) {
        String answerText = aiService.generateText(question);
        return answerText;
    }
    

}
