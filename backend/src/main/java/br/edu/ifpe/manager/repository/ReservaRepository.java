package br.edu.ifpe.manager.repository;

import br.edu.ifpe.manager.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Busca reservas por usuário
    List<Reserva> findByUsuarioId(Long usuarioId);

    // Busca reservas por recurso
    List<Reserva> findByRecursoId(Long recursoId);

    // Busca reservas em um intervalo de tempo
    @Query("""
           SELECT r FROM Reserva r 
           WHERE r.dataInicio <= :dataFinal AND r.dataFinal >= :dataInicio
           """)
    List<Reserva> findReservasPorIntervalo(@Param("dataInicio") LocalDateTime dataInicio, 
                                           @Param("dataFinal") LocalDateTime dataFinal);

    // Busca reservas pendentes para aprovação
    @Query("""
           SELECT r FROM Reserva r 
           WHERE r.status = 'PENDENTE'
           """)
    List<Reserva> findReservasPendentes();
}
