package br.edu.ifpe.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.Reserva;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    
    List<Reserva> findByRecursoAndDataInicioBeforeAndDataFimAfter(
            Recurso recurso,
            LocalDateTime dataFim,
            LocalDateTime dataInicio
    );
}
