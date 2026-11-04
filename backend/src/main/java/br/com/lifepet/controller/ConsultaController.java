package br.com.lifepet.controller;

import br.com.lifepet.entity.Consulta;
import br.com.lifepet.service.ConsultaService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {


    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

// =========================================================
// LISTAR CONSULTAS
// =========================================================

    @GetMapping
    @PreAuthorize("hasAnyRole('TUTOR', 'VETERINARIO')")
    public ResponseEntity<Page<Consulta>> listar(Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

// =========================================================
// BUSCAR POR ID
// =========================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TUTOR', 'VETERINARIO')")
    public ResponseEntity<Consulta> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

// =========================================================
// TUTOR - SOLICITAR / AGENDAR CONSULTA
// =========================================================

    @PostMapping
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<Consulta> agendar(
            @Valid @RequestBody Consulta consulta) {

        return ResponseEntity.ok(service.agendar(consulta));
    }

// =========================================================
// VETERINÁRIO - ATUALIZAR CONSULTA
// =========================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<Consulta> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Consulta consulta) {

        service.buscarPorId(id);

        consulta.setId(id);

        return ResponseEntity.ok(service.salvar(consulta));
    }

// =========================================================
// VETERINÁRIO - CONFIRMAR
// =========================================================

    @PutMapping("/{id}/confirmar")
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<Consulta> confirmar(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.confirmar(id));
    }

// =========================================================
// VETERINÁRIO - REALIZAR
// =========================================================

    @PutMapping("/{id}/realizar")
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<Consulta> realizar(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.realizar(id));
    }

// =========================================================
// VETERINÁRIO - CANCELAR
// =========================================================

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<Consulta> cancelar(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.cancelar(id));
    }

// =========================================================
// VETERINÁRIO - DELETAR
// =========================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }


}

