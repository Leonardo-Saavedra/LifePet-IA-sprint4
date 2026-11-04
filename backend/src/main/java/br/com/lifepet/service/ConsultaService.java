package br.com.lifepet.service;

import br.com.lifepet.entity.Consulta;
import br.com.lifepet.entity.StatusConsulta;
import br.com.lifepet.exception.ResourceNotFoundException;
import br.com.lifepet.repository.ConsultaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository repository;

    public Page<Consulta> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Consulta buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consulta com ID " + id + " não encontrada"
                ));
    }

    public Consulta salvar(Consulta consulta) {
        return repository.save(consulta);
    }

    public Consulta agendar(Consulta consulta) {

        if (consulta.getPet() == null) {
            throw new IllegalArgumentException(
                    "A consulta precisa estar vinculada a um pet"
            );
        }

        if (consulta.getPet().getId() == null) {
            throw new IllegalArgumentException(
                    "O pet precisa possuir um ID válido"
            );
        }

        if (consulta.getData() == null) {
            throw new IllegalArgumentException(
                    "A data da consulta é obrigatória"
            );
        }

        if (consulta.getData().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Não é possível agendar uma consulta para uma data anterior à atual"
            );
        }

        boolean consultaExistente =
                repository.existsByPetIdAndData(
                        consulta.getPet().getId(),
                        consulta.getData()
                );

        if (consultaExistente) {
            throw new IllegalArgumentException(
                    "Este pet já possui uma consulta agendada para esta data"
            );
        }

        consulta.setStatus(StatusConsulta.AGENDADA);

        return repository.save(consulta);
    }

    public Consulta confirmar(Long id) {

        Consulta consulta = buscarPorId(id);

        if (consulta.getStatus() != StatusConsulta.AGENDADA) {
            throw new IllegalArgumentException(
                    "Apenas consultas com status AGENDADA podem ser confirmadas"
            );
        }

        consulta.setStatus(StatusConsulta.CONFIRMADA);

        return repository.save(consulta);
    }

    public Consulta realizar(Long id) {

        Consulta consulta = buscarPorId(id);

        if (consulta.getStatus() != StatusConsulta.CONFIRMADA) {
            throw new IllegalArgumentException(
                    "Apenas consultas CONFIRMADAS podem ser marcadas como REALIZADAS"
            );
        }

        consulta.setStatus(StatusConsulta.REALIZADA);

        return repository.save(consulta);
    }

    public Consulta cancelar(Long id) {

        Consulta consulta = buscarPorId(id);

        if (consulta.getStatus() == StatusConsulta.REALIZADA) {
            throw new IllegalArgumentException(
                    "Não é possível cancelar uma consulta que já foi realizada"
            );
        }

        if (consulta.getStatus() == StatusConsulta.CANCELADA) {
            throw new IllegalArgumentException(
                    "Esta consulta já está cancelada"
            );
        }

        consulta.setStatus(StatusConsulta.CANCELADA);

        return repository.save(consulta);
    }

    public void deletar(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Consulta com ID " + id + " não encontrada"
            );
        }

        repository.deleteById(id);
    }
}

