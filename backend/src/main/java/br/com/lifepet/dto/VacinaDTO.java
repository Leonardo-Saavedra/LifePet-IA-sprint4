package br.com.lifepet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class VacinaDTO {

    private Long id;

    @NotBlank(message = "Nome da vacina é obrigatório")
    private String nome;

    @NotNull(message = "Data de aplicação é obrigatória")
    private LocalDate dataAplicacao;

    @NotNull(message = "Próxima dose é obrigatória")
    private LocalDate proximaDose;

    @NotNull(message = "Pet é obrigatório")
    private Long petId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }


    public LocalDate getProximaDose() {
        return proximaDose;
    }

    public void setProximaDose(LocalDate proximaDose) {
        this.proximaDose = proximaDose;
    }


    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }
}