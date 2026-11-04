        package br.com.lifepet.controller;

import br.com.lifepet.dto.HistoricoDTO;
import br.com.lifepet.entity.Historico;
import br.com.lifepet.service.HistoricoService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Histórico Clínico",
        description = "Gerenciamento do histórico clínico dos pets"
)

@RestController
@RequestMapping("/historicos")
public class HistoricoController {

    @Autowired
    private HistoricoService service;


    // LISTAR COM PAGINAÇÃO
    @Operation(summary = "Listar históricos com paginação")
    @PreAuthorize("hasRole('VETERINARIO')")
    @GetMapping
    public Page<Historico> listar(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size

    ) {

        Pageable pageable = PageRequest.of(page, size);

        return service.listar(pageable);
    }


    // BUSCAR POR ID
    @Operation(summary = "Buscar histórico por ID")
    @PreAuthorize("hasRole('VETERINARIO')")
    @GetMapping("/{id}")
    public ResponseEntity<Historico> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }


    // CADASTRAR
    @Operation(summary = "Cadastrar novo histórico")
    @PreAuthorize("hasRole('VETERINARIO')")
    @PostMapping
    public ResponseEntity<HistoricoDTO> salvar(
            @Valid @RequestBody HistoricoDTO dto) {

        Historico historico = new Historico();

        historico.setDescricao(dto.getDescricao());
        historico.setDataRegistro(dto.getDataRegistro());
        historico.setObservacoes(dto.getObservacoes());

        Historico salvo = service.salvar(historico);

        HistoricoDTO resposta = new HistoricoDTO();

        resposta.setId(salvo.getId());
        resposta.setDescricao(salvo.getDescricao());
        resposta.setDataRegistro(salvo.getDataRegistro());
        resposta.setObservacoes(salvo.getObservacoes());

        return ResponseEntity.ok(resposta);
    }


    // EXCLUIR
    @Operation(summary = "Excluir histórico")
    @PreAuthorize("hasRole('VETERINARIO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}

