package br.edu.ifpe.manager.repository;

import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Consulta para verificar conflitos de reserva
    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.recurso = :recurso " +
           "AND ((r.startDate BETWEEN :startDate AND :endDate) " +
           "OR (r.endDate BETWEEN :startDate AND :endDate))")
    boolean existsReservaInPeriod(
        @Param("recurso") Recurso recurso, 
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );

    // Consultar todas as reservas de um recurso
    List<Reserva> findByRecurso(Recurso recurso);

    // Consulta para encontrar reservas que já terminaram
    @Query("SELECT r FROM Reserva r WHERE r.endDate < :now")
    List<Reserva> findAllByEndDateBefore(@Param("now") LocalDateTime now);
}
