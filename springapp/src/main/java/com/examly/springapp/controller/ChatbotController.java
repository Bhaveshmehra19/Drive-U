package com.examly.springapp.controller;

import com.examly.springapp.model.ChatRequest;
import com.examly.springapp.model.ChatResponse;
import com.examly.springapp.service.GeminiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final GeminiService geminiService;

    public ChatbotController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> askQuestion(
            @RequestBody ChatRequest chatRequest) {

        if (chatRequest == null
                || chatRequest.getMessage() == null
                || chatRequest.getMessage().trim().isEmpty()) {

            ChatResponse errorResponse = new ChatResponse(
                    "Please enter a valid question related to the DriveU project."
            );

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse);
        }

        String answer = geminiService.getChatbotResponse(
                chatRequest.getMessage()
        );

        return ResponseEntity.ok(new ChatResponse(answer));
    }
}


// POST http://localhost:8080/api/chatbot/ask