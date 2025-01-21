package br.edu.ifpe.manager.repository;

import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.StatusReserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
	// Busca reservas por recurso
	List<Reserva> findByRecursoId(Long recursoId);

	// Busca reservas por status
	List<Reserva> findByStatus(String status);

	// Busca reservas com status específico e cuja data_fim já passou
	List<Reserva> findByStatusAndDataFimBefore(StatusReserva status, LocalDateTime dataFim);
}
