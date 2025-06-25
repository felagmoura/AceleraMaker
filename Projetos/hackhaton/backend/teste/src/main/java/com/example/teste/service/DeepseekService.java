package com.example.teste.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Service
public class DeepseekService {
    @Value("${deepseek.api.key}")

    private String apiKey;
    private final String API_URL = "https://api.deepseek.com/v1/chat/completions";

    private final RestTemplate RestTemplate =  new RestTemplate();

    public String getDeepSeekResponse(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "deepseek-chat");
        body.put("messages", List.of(Map.of("role", "user", "content", prompt)));


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = RestTemplate.postForEntity(API_URL, entity, Map.class);

        return response.getBody().get("choices").toString();
    }
}
