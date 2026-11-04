package br.com.lifepet.controller;

import br.com.lifepet.dto.ChatRequestDTO;
import br.com.lifepet.dto.ChatResponseDTO;
import br.com.lifepet.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatResponseDTO conversar(@Valid @RequestBody ChatRequestDTO requisicao) {
        return chatService.conversar(requisicao);
    }
}