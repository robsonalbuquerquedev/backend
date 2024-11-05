package br.edu.ifpe.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.manager.model.Sala;
import br.edu.ifpe.manager.model.StatusSala;

import java.util.List;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    List<Sala> findByStatus(StatusSala status); // Para buscar salas com base no status (disponível ou indisponível)
}
