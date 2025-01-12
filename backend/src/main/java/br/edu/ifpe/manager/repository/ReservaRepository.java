package br.edu.ifpe.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.manager.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}
