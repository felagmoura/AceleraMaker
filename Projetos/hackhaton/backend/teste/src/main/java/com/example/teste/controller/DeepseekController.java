package com.example.teste.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.teste.service.DeepseekService;

@RestController
@RequestMapping("/api/deepseek")
public class DeepseekController {

    private final DeepseekService deepseekService;

    public DeepseekController(DeepseekService deepseekService) {
        this.deepseekService = deepseekService;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody String prompt) {
        String response = deepseekService.getDeepSeekResponse(prompt);
        return ResponseEntity.ok(response);
    }
}