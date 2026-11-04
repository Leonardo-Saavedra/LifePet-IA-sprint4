package br.com.lifepet.repository;

import br.com.lifepet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByNomeContainingIgnoreCase(String nome);

    List<Pet> findByEspecieContainingIgnoreCase(String especie);
}
