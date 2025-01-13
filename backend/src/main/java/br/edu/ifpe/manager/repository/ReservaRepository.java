package br.edu.ifpe.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpe.manager.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    // Métodos customizados, se necessário
}
