        package br.com.lifepet.controller;

import br.com.lifepet.dto.TutorDTO;
import br.com.lifepet.entity.Tutor;
import br.com.lifepet.service.TutorService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@Tag(
        name = "Tutores",
        description = "Gerenciamento de tutores do sistema LifePet"
)

@RestController
@RequestMapping("/tutores")
public class TutorController {

    @Autowired
    private TutorService tutorService;


    // LISTAR
    @Operation(summary = "Listar tutores com paginação")
    @PreAuthorize("hasRole('TUTOR')")
    @GetMapping
    public Page<Tutor> listarTodos(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "nome") String sort

    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort)
        );

        return tutorService.listar(pageable);
    }


    // BUSCAR POR ID
    @Operation(summary = "Buscar tutor por ID")
    @PreAuthorize("hasRole('TUTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Tutor> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                tutorService.buscarPorId(id).get()
        );
    }


    // BUSCAR POR NOME
    @Operation(summary = "Buscar tutor por nome")
    @PreAuthorize("hasRole('TUTOR')")
    @GetMapping("/buscar/nome")
    public List<Tutor> buscarPorNome(
            @RequestParam String nome) {

        return tutorService.buscarPorNome(nome);
    }


    // BUSCAR POR EMAIL
    @Operation(summary = "Buscar tutor por email")
    @PreAuthorize("hasRole('TUTOR')")
    @GetMapping("/buscar/email")
    public List<Tutor> buscarPorEmail(
            @RequestParam String email) {

        return tutorService.buscarPorEmail(email);
    }


    // CADASTRAR
    @Operation(summary = "Cadastrar novo tutor")
    @PreAuthorize("hasRole('TUTOR')")
    @PostMapping
    public TutorDTO cadastrar(
            @Valid @RequestBody TutorDTO dto) {

        Tutor tutor = new Tutor();

        tutor.setNome(dto.getNome());
        tutor.setEmail(dto.getEmail());
        tutor.setTelefone(dto.getTelefone());

        Tutor salvo = tutorService.salvar(tutor);

        TutorDTO resposta = new TutorDTO();

        resposta.setId(salvo.getId());
        resposta.setNome(salvo.getNome());
        resposta.setEmail(salvo.getEmail());
        resposta.setTelefone(salvo.getTelefone());

        return resposta;
    }


    // ATUALIZAR
    @Operation(summary = "Atualizar tutor")
    @PreAuthorize("hasRole('TUTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Tutor> atualizar(

            @PathVariable Long id,
            @Valid @RequestBody Tutor tutor

    ) {

        return ResponseEntity.ok(
                tutorService.atualizar(id, tutor)
        );
    }


    // DELETAR
    @Operation(summary = "Excluir tutor")
    @PreAuthorize("hasRole('TUTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        tutorService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
