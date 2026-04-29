package com.spring.ai.firstproject.Controller;

import com.spring.ai.firstproject.Service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping
public class ChatController {
    private ChatService chatService;

    public  ChatController(ChatService chatService){
        this.chatService=chatService;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value = "q") String userQuery
      ){

        return ResponseEntity.ok(chatService.getResponse(userQuery));

    }

}
