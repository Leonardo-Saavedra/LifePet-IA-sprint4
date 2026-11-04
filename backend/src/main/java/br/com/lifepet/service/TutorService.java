package br.com.lifepet.service;

import br.com.lifepet.entity.Tutor;
import br.com.lifepet.exception.ResourceNotFoundException;
import br.com.lifepet.repository.TutorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TutorService {

    @Autowired
    private TutorRepository tutorRepository;

    // LISTAR COM PAGINAÇÃO
    public Page<Tutor> listar(Pageable pageable) {
        return tutorRepository.findAll(pageable);
    }

    // BUSCAR POR ID
    public Optional<Tutor> buscarPorId(Long id) {

        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tutor com ID " + id + " não encontrado"
                        ));

        return Optional.of(tutor);
    }

    // SALVAR
    public Tutor salvar(Tutor tutor) {
        return tutorRepository.save(tutor);
    }

    // ATUALIZAR
    public Tutor atualizar(Long id, Tutor tutorAtualizado) {

        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tutor com ID " + id + " não encontrado"
                        ));

        tutor.setNome(tutorAtualizado.getNome());
        tutor.setEmail(tutorAtualizado.getEmail());
        tutor.setTelefone(tutorAtualizado.getTelefone());

        return tutorRepository.save(tutor);
    }

    // DELETAR
    public void deletar(Long id) {

        if (!tutorRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Tutor com ID " + id + " não encontrado"
            );
        }

        tutorRepository.deleteById(id);
    }

    // BUSCAR POR NOME
    public List<Tutor> buscarPorNome(String nome) {
        return tutorRepository.findByNomeContainingIgnoreCase(nome);
    }

    // BUSCAR POR EMAIL
    public List<Tutor> buscarPorEmail(String email) {
        return tutorRepository.findByEmailContainingIgnoreCase(email);
    }
}