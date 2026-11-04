package br.com.lifepet.controller;

import br.com.lifepet.dto.VacinaDTO;
import br.com.lifepet.entity.Pet;
import br.com.lifepet.entity.Vacina;
import br.com.lifepet.service.PetService;
import br.com.lifepet.service.VacinaService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Vacinas",
        description = "Gerenciamento de vacinas dos pets"
)
@RestController
@RequestMapping("/vacinas")
public class VacinaController {

    @Autowired
    private VacinaService service;

    @Autowired
    private PetService petService;


    // =========================================================
    // LISTAR COM PAGINAÇÃO
    // =========================================================

    @Operation(summary = "Listar vacinas com paginação")
    @PreAuthorize("hasRole('VETERINARIO')")
    @GetMapping
    public Page<Vacina> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "nome") String sort
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort)
        );

        return service.listar(pageable);
    }


    // =========================================================
    // BUSCAR VACINAS DE UM PET
    // =========================================================

    @Operation(summary = "Buscar vacinas de um pet")
    @PreAuthorize("hasRole('VETERINARIO')")
    @GetMapping("/pet/{petId}")
    public List<Vacina> buscarPorPet(
            @PathVariable Long petId
    ) {

        return service.buscarPorPet(petId);
    }


    // =========================================================
    // VACINAS ATRASADAS
    // =========================================================

    @Operation(summary = "Listar vacinas atrasadas")
    @PreAuthorize("hasRole('VETERINARIO')")
    @GetMapping("/atrasadas")
    public List<Vacina> vacinasAtrasadas() {

        return service.listarAtrasadas();
    }


    // =========================================================
    // VACINAS EM DIA
    // =========================================================

    @Operation(summary = "Listar vacinas em dia")
    @PreAuthorize("hasRole('VETERINARIO')")
    @GetMapping("/em-dia")
    public List<Vacina> vacinasEmDia() {

        return service.listarEmDia();
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    @Operation(summary = "Buscar vacina por ID")
    @PreAuthorize("hasRole('VETERINARIO')")
    @GetMapping("/{id}")
    public ResponseEntity<Vacina> buscarPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }


    // =========================================================
    // BUSCAR POR NOME
    // =========================================================

    @Operation(summary = "Buscar vacina por nome")
    @PreAuthorize("hasRole('VETERINARIO')")
    @GetMapping("/buscar")
    public List<Vacina> buscarPorNome(
            @RequestParam String nome
    ) {

        return service.buscarPorNome(nome);
    }


    // =========================================================
    // CADASTRAR
    // =========================================================

    @Operation(summary = "Cadastrar nova vacina")
    @PreAuthorize("hasRole('VETERINARIO')")
    @PostMapping
    public ResponseEntity<VacinaDTO> salvar(
            @Valid @RequestBody VacinaDTO dto
    ) {

        Vacina vacina = new Vacina();

        vacina.setNome(dto.getNome());
        vacina.setDataAplicacao(dto.getDataAplicacao());
        vacina.setProximaDose(dto.getProximaDose());

        if (dto.getPetId() != null) {

            Pet pet = petService.buscarPorId(dto.getPetId());

            vacina.setPet(pet);
        }

        Vacina salva = service.salvar(vacina);

        VacinaDTO resposta = new VacinaDTO();

        resposta.setId(salva.getId());
        resposta.setNome(salva.getNome());
        resposta.setDataAplicacao(salva.getDataAplicacao());
        resposta.setProximaDose(salva.getProximaDose());

        if (salva.getPet() != null) {

            resposta.setPetId(
                    salva.getPet().getId()
            );
        }

        return ResponseEntity.ok(resposta);
    }


    // =========================================================
    // ATUALIZAR
    // =========================================================

    @Operation(summary = "Atualizar vacina")
    @PreAuthorize("hasRole('VETERINARIO')")
    @PutMapping("/{id}")
    public ResponseEntity<Vacina> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Vacina vacina
    ) {

        vacina.setId(id);

        return ResponseEntity.ok(
                service.salvar(vacina)
        );
    }


    // =========================================================
    // EXCLUIR
    // =========================================================

    @Operation(summary = "Excluir vacina")
    @PreAuthorize("hasRole('VETERINARIO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id
    ) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
