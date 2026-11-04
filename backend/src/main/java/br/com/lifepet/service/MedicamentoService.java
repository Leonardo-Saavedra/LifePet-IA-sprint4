package br.com.lifepet.service;

import br.com.lifepet.entity.Medicamento;
import br.com.lifepet.exception.ResourceNotFoundException;
import br.com.lifepet.repository.MedicamentoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository repository;

    // LISTAR COM PAGINAÇÃO
    public Page<Medicamento> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // BUSCAR POR ID
    public Medicamento buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medicamento não encontrado com ID: " + id
                        )
                );
    }

    // LISTAR MEDICAMENTOS DE UM PET
    public List<Medicamento> listarPorPet(Long petId) {
        return repository.findByPetId(petId);
    }

    // SALVAR COM REGRAS DE NEGÓCIO
    public Medicamento salvar(Medicamento medicamento) {

        if (medicamento.getPet() == null) {
            throw new IllegalArgumentException(
                    "O medicamento precisa estar vinculado a um pet"
            );
        }

        if (medicamento.getPet().getId() == null) {
            throw new IllegalArgumentException(
                    "O pet precisa possuir um ID válido"
            );
        }

        if (medicamento.getDataInicio() == null) {
            throw new IllegalArgumentException(
                    "A data de início é obrigatória"
            );
        }

        if (medicamento.getDataFim() == null) {
            throw new IllegalArgumentException(
                    "A data final é obrigatória"
            );
        }

        if (medicamento.getDataFim()
                .isBefore(medicamento.getDataInicio())) {

            throw new IllegalArgumentException(
                    "A data final não pode ser anterior à data de início"
            );
        }

        return repository.save(medicamento);
    }

    // DELETAR
    public void deletar(Long id) {

        Medicamento medicamento = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medicamento não encontrado com ID: " + id
                        )
                );

        repository.delete(medicamento);
    }

    // VERIFICAR SE O MEDICAMENTO ESTÁ ATIVO
    public boolean estaAtivo(Medicamento medicamento) {

        LocalDate hoje = LocalDate.now();

        return !hoje.isBefore(medicamento.getDataInicio())
                && !hoje.isAfter(medicamento.getDataFim());
    }
}