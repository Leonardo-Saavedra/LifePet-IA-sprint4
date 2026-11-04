        package br.com.lifepet.controller;

import br.com.lifepet.dto.PetDTO;
import br.com.lifepet.entity.Pet;
import br.com.lifepet.entity.Tutor;
import br.com.lifepet.service.PetService;
import br.com.lifepet.service.TutorService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Pets",
        description = "Gerenciamento de pets do sistema LifePet"
)

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private TutorService tutorService;


    // =========================================================
    // LISTAR COM PAGINAÇÃO
    // =========================================================

    @Operation(summary = "Listar pets com paginação")
    @PreAuthorize("hasAnyRole('TUTOR', 'VETERINARIO')")
    @GetMapping
    public Page<Pet> listar(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size

    ) {

        Pageable pageable = PageRequest.of(
                page,
                size
        );

        return petService.listar(pageable);
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    @Operation(summary = "Buscar pet por ID")
    @PreAuthorize("hasAnyRole('TUTOR', 'VETERINARIO')")
    @GetMapping("/{id}")
    public ResponseEntity<Pet> buscarPorId(
            @PathVariable Long id) {

        Pet pet = petService.buscarPorId(id);

        return ResponseEntity.ok(pet);
    }


    // =========================================================
    // CADASTRAR PET
    // =========================================================

    @Operation(summary = "Cadastrar novo pet")
    @PreAuthorize("hasRole('TUTOR')")
    @PostMapping
    public ResponseEntity<PetDTO> cadastrar(
            @Valid @RequestBody PetDTO dto) {

        Pet pet = new Pet();

        pet.setNome(dto.getNome());
        pet.setIdade(dto.getIdade());
        pet.setRaca(dto.getRaca());
        pet.setEspecie(dto.getEspecie());
        pet.setPeso(dto.getPeso());

        Tutor tutor = tutorService.buscarPorId(dto.getTutorId())
                .orElseThrow(
                        () -> new RuntimeException("Tutor não encontrado")
                );

        pet.setTutor(tutor);

        Pet salvo = petService.salvar(pet);

        PetDTO resposta = new PetDTO();

        resposta.setId(salvo.getId());
        resposta.setNome(salvo.getNome());
        resposta.setIdade(salvo.getIdade());
        resposta.setRaca(salvo.getRaca());
        resposta.setEspecie(salvo.getEspecie());
        resposta.setPeso(salvo.getPeso());
        resposta.setTutorId(salvo.getTutor().getId());

        return ResponseEntity.ok(resposta);
    }


    // =========================================================
    // ATUALIZAR
    // =========================================================

    @Operation(summary = "Atualizar pet")
    @PreAuthorize("hasRole('TUTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Pet> atualizar(

            @PathVariable Long id,
            @Valid @RequestBody Pet pet

    ) {

        return ResponseEntity.ok(
                petService.atualizar(id, pet)
        );
    }


    // =========================================================
    // DELETAR
    // =========================================================

    @Operation(summary = "Excluir pet")
    @PreAuthorize("hasRole('TUTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        petService.deletar(id);

        return ResponseEntity.noContent().build();
    }


    // =========================================================
    // BUSCAR POR NOME
    // =========================================================

    @Operation(summary = "Buscar pet por nome")
    @PreAuthorize("hasAnyRole('TUTOR', 'VETERINARIO')")
    @GetMapping("/buscar/nome")
    public List<Pet> buscarPorNome(
            @RequestParam String nome) {

        return petService.buscarPorNome(nome);
    }


    // =========================================================
    // BUSCAR POR ESPÉCIE
    // =========================================================

    @Operation(summary = "Buscar pet por espécie")
    @PreAuthorize("hasAnyRole('TUTOR', 'VETERINARIO')")
    @GetMapping("/buscar/especie")
    public List<Pet> buscarPorEspecie(
            @RequestParam String especie) {

        return petService.buscarPorEspecie(especie);
    }


    // =========================================================
    // RECOMENDAÇÕES
    // =========================================================

    @Operation(summary = "Gerar recomendações para o pet")
    @PreAuthorize("hasRole('TUTOR')")
    @GetMapping("/{id}/recomendacoes")
    public String recomendacoes(
            @PathVariable Long id) {

        Pet pet = petService.buscarPorId(id);

        if (pet.getIdade() < 1) {
            return "Pet filhote: recomendamos vacinas V8/V10 e acompanhamento mensal.";
        }

        if (pet.getIdade() >= 7) {
            return "Pet idoso: check-up recomendado a cada 6 meses.";
        }

        return "Pet saudável: manter consultas e vacinas em dia.";
    }
}


