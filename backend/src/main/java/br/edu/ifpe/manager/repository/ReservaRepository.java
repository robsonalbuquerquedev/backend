package br.edu.ifpe.manager.repository;

import br.edu.ifpe.manager.model.Reserva;
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

    // Busca reservas em um intervalo de tempo
    List<Reserva> findByDataInicioBetween(LocalDateTime inicio, LocalDateTime fim);
}
