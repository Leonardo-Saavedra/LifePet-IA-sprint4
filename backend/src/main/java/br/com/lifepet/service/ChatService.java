package br.com.lifepet.service;

import br.com.lifepet.dto.ChatRequestDTO;
import br.com.lifepet.dto.ChatResponseDTO;
import br.com.lifepet.entity.Pet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatService {

    private final PetService petService;
    private final RestClient restClient;

    public ChatService(
            PetService petService,
            RestClient.Builder builder,
            @Value("${vetbot.api.url}") String vetbotUrl
    ) {
        this.petService = petService;
        this.restClient = builder.baseUrl(vetbotUrl).build();
    }

    public ChatResponseDTO conversar(ChatRequestDTO requisicao) {
        Pet pet = petService.buscarPorId(requisicao.petId());

        Map<String, Object> corpo = new HashMap<>();
        corpo.put("pergunta", requisicao.pergunta());
        corpo.put("pet", pet);
        corpo.put("conversaId", requisicao.conversaId());

        try {
            return restClient.post()
                    .uri("/chat")
                    .body(corpo)
                    .retrieve()
                    .body(ChatResponseDTO.class);
        } catch (RestClientException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "API de IA indisponível ou com falha: " + e.getMessage()
            );
        }
    }
}