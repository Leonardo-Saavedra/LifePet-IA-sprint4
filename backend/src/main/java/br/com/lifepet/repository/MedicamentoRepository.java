package br.com.lifepet.repository;

import br.com.lifepet.entity.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    List<Medicamento> findByPetId(Long petId);

}
