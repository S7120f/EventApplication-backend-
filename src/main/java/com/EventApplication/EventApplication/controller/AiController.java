package com.EventApplication.EventApplication.controller;

import com.EventApplication.EventApplication.dto.AiChatRequest;
import com.EventApplication.EventApplication.service.ChatServiceAi;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = {
        "http://localhost:4200", //  lokala Angular
        "https://stingray-app-fe45r.ondigitalocean.app" //
})
public class AiController {

    private final ChatServiceAi chatServiceAi;

    public AiController(ChatServiceAi chatServiceAi) {
        this.chatServiceAi = chatServiceAi;
    }

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody AiChatRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(chatServiceAi.chat(request.getPrompt()));
    }
}
