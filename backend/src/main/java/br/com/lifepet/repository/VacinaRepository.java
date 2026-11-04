package br.com.lifepet.repository;

import br.com.lifepet.entity.Vacina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacinaRepository extends JpaRepository<Vacina, Long> {

    List<Vacina> findByNomeContainingIgnoreCase(String nome);

    List<Vacina> findByPetId(Long petId);
}