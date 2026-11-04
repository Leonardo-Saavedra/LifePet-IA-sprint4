package br.com.lifepet.controller;

import br.com.lifepet.dto.MedicamentoDTO;
import br.com.lifepet.entity.Medicamento;
import br.com.lifepet.entity.Pet;
import br.com.lifepet.service.MedicamentoService;
import br.com.lifepet.service.PetService;

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

import java.time.LocalDate;
import java.util.List;

@Tag(
        name = "Medicamentos",
        description = "Gerenciamento de medicamentos dos pets"
)
@RestController
@RequestMapping("/medicamentos")
public class MedicamentoController {

    @Autowired
    private MedicamentoService service;

    @Autowired
    private PetService petService;

    // =====================================================
    // LISTAR MEDICAMENTOS ATIVOS
    // =====================================================

    @Operation(summary = "Listar medicamentos ativos")
    @PreAuthorize("hasRole('VETERINARIO')")
    @GetMapping("/ativos")
    public List<Medicamento> medicamentosAtivos() {

        return service.listar(Pageable.unpaged())
                .stream()
                .filter(m -> m.getDataInicio() != null
                        && m.getDataFim() != null
                        && !LocalDate.now().isBefore(m.getDataInicio())
                        && !LocalDate.now().isAfter(m.getDataFim()))
                .toList();
    }

    // =====================================================
    // LISTAR MEDICAMENTOS COM PAGINAÇÃO
    // =====================================================

    @Operation(summary = "Listar medicamentos com paginação")
    @PreAuthorize("hasRole('VETERINARIO')")
    @GetMapping
    public Page<Medicamento> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return service.listar(pageable);
    }

    // =====================================================
    // BUSCAR MEDICAMENTO POR ID
    // =====================================================

    @Operation(summary = "Buscar medicamento por ID")
    @PreAuthorize("hasRole('VETERINARIO')")
    @GetMapping("/{id}")
    public ResponseEntity<Medicamento> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    // =====================================================
    // LISTAR MEDICAMENTOS DE UM PET
    // =====================================================

    @Operation(summary = "Listar medicamentos de um pet")
    @PreAuthorize("hasAnyRole('TUTOR', 'VETERINARIO')")
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<Medicamento>> listarPorPet(
            @PathVariable Long petId) {

        petService.buscarPorId(petId);

        return ResponseEntity.ok(
                service.listarPorPet(petId)
        );
    }

    // =====================================================
    // CADASTRAR MEDICAMENTO
    // =====================================================

    @Operation(summary = "Cadastrar novo medicamento")
    @PreAuthorize("hasRole('VETERINARIO')")
    @PostMapping
    public ResponseEntity<MedicamentoDTO> salvar(
            @Valid @RequestBody MedicamentoDTO dto) {

        Medicamento medicamento = new Medicamento();

        medicamento.setNome(dto.getNome());
        medicamento.setDosagem(dto.getDosagem());
        medicamento.setFrequencia(dto.getFrequencia());
        medicamento.setDataInicio(dto.getDataInicio());
        medicamento.setDataFim(dto.getDataFim());

        if (dto.getPetId() != null) {

            Pet pet = petService.buscarPorId(dto.getPetId());

            medicamento.setPet(pet);
        }

        Medicamento salvo = service.salvar(medicamento);

        MedicamentoDTO resposta = new MedicamentoDTO();

        resposta.setId(salvo.getId());
        resposta.setNome(salvo.getNome());
        resposta.setDosagem(salvo.getDosagem());
        resposta.setFrequencia(salvo.getFrequencia());
        resposta.setDataInicio(salvo.getDataInicio());
        resposta.setDataFim(salvo.getDataFim());

        if (salvo.getPet() != null) {

            resposta.setPetId(
                    salvo.getPet().getId()
            );
        }

        return ResponseEntity.ok(resposta);
    }

    // =====================================================
    // EXCLUIR MEDICAMENTO
    // =====================================================

    @Operation(summary = "Excluir medicamento")
    @PreAuthorize("hasRole('VETERINARIO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}

