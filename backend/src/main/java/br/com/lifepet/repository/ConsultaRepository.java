        package br.com.lifepet.repository;

import br.com.lifepet.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    boolean existsByPetIdAndData(Long petId, LocalDate data);

}

