package br.com.lifepet.repository;

import br.com.lifepet.entity.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {
}
