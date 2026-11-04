package br.com.lifepet.service;

import br.com.lifepet.entity.Vacina;
import br.com.lifepet.exception.ResourceNotFoundException;
import br.com.lifepet.repository.VacinaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VacinaService {

    @Autowired
    private VacinaRepository repository;

    // LISTAR COM PAGINAÇÃO
    public Page<Vacina> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // BUSCAR POR ID
    public Vacina buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vacina não encontrada com ID: " + id
                        )
                );
    }

    // SALVAR
    public Vacina salvar(Vacina vacina) {
        return repository.save(vacina);
    }

    // DELETAR COM VALIDAÇÃO
    public void deletar(Long id) {

        Vacina vacina = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vacina não encontrada com ID: " + id
                        )
                );

        repository.delete(vacina);
    }

    // BUSCAR POR NOME
    public List<Vacina> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    // =========================================================
    // FLUXO DE NEGÓCIO 2 - CONTROLE DE VACINAÇÃO
    // =========================================================

    // BUSCAR TODAS AS VACINAS DE UM PET
    public List<Vacina> buscarPorPet(Long petId) {

        List<Vacina> vacinas = repository.findByPetId(petId);

        if (vacinas.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Nenhuma vacina encontrada para o pet com ID: " + petId
            );
        }

        return vacinas;
    }

    // LISTAR VACINAS ATRASADAS
    public List<Vacina> listarAtrasadas() {

        LocalDate hoje = LocalDate.now();

        return repository.findAll()
                .stream()
                .filter(vacina ->
                        vacina.getProximaDose() != null
                                && vacina.getProximaDose().isBefore(hoje)
                )
                .toList();
    }

    // LISTAR VACINAS EM DIA
    public List<Vacina> listarEmDia() {

        LocalDate hoje = LocalDate.now();

        return repository.findAll()
                .stream()
                .filter(vacina ->
                        vacina.getProximaDose() != null
                                && !vacina.getProximaDose().isBefore(hoje)
                )
                .toList();
    }
}