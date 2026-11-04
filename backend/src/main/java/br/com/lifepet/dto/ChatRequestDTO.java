package br.com.lifepet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatRequestDTO(
        @NotBlank(message = "Pergunta é obrigatória")
        String pergunta,

        @NotNull(message = "Id do pet é obrigatório")
        Long petId,

        String conversaId
) {
}