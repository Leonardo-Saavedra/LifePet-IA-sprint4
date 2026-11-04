package br.com.lifepet.service;

import br.com.lifepet.entity.Historico;
import br.com.lifepet.exception.ResourceNotFoundException;
import br.com.lifepet.repository.HistoricoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HistoricoService {

    @Autowired
    private HistoricoRepository repository;

    // LISTAR COM PAGINAÇÃO
    public Page<Historico> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // BUSCAR POR ID
    public Historico buscarPorId(Long id) {

        return repository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Histórico com ID " + id + " não encontrado"
                        )
                );
    }

    // SALVAR
    public Historico salvar(Historico historico) {
        return repository.save(historico);
    }

    // DELETAR
    public void deletar(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Histórico com ID " + id + " não encontrado"
            );
        }

        repository.deleteById(id);
    }
}