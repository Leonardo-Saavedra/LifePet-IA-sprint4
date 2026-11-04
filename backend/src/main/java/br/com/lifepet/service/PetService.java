package br.com.lifepet.service;

import br.com.lifepet.entity.Pet;
import br.com.lifepet.exception.PetNotFoundException;
import br.com.lifepet.repository.PetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    // LISTAR COM PAGINAÇÃO + CACHE
    @Cacheable("pets")
    public Page<Pet> listar(Pageable pageable) {

        return petRepository.findAll(pageable);
    }

    // BUSCAR POR ID
    public Pet buscarPorId(Long id) {

        return petRepository.findById(id)
                .orElseThrow(
                        () -> new PetNotFoundException(id)
                );
    }

    // SALVAR + LIMPAR CACHE
    @CacheEvict(value = "pets", allEntries = true)
    public Pet salvar(Pet pet) {

        return petRepository.save(pet);
    }

    // ATUALIZAR + LIMPAR CACHE
    @CacheEvict(value = "pets", allEntries = true)
    public Pet atualizar(Long id, Pet petAtualizado) {

        Pet pet = petRepository.findById(id)
                .orElseThrow(
                        () -> new PetNotFoundException(id)
                );

        pet.setNome(petAtualizado.getNome());
        pet.setEspecie(petAtualizado.getEspecie());
        pet.setRaca(petAtualizado.getRaca());
        pet.setIdade(petAtualizado.getIdade());
        pet.setPeso(petAtualizado.getPeso());
        pet.setTutor(petAtualizado.getTutor());

        return petRepository.save(pet);
    }

    // DELETAR + LIMPAR CACHE
    @CacheEvict(value = "pets", allEntries = true)
    public void deletar(Long id) {

        if (!petRepository.existsById(id)) {
            throw new PetNotFoundException(id);
        }

        petRepository.deleteById(id);
    }

    // BUSCAR POR NOME
    public List<Pet> buscarPorNome(String nome) {

        return petRepository.findByNomeContainingIgnoreCase(nome);
    }

    // BUSCAR POR ESPÉCIE
    public List<Pet> buscarPorEspecie(String especie) {

        return petRepository.findByEspecieContainingIgnoreCase(especie);
    }
}
