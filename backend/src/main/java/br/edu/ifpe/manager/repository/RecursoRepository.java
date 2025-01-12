package br.edu.ifpe.manager.repository;

import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long> {
    
    // Busca recursos pelo nome (parcial, ignorando maiúsculas/minúsculas)
    List<Recurso> findByNomeContainingIgnoreCase(String nome);

    // Busca recursos por localização (parcial, ignorando maiúsculas/minúsculas)
    List<Recurso> findByLocalizacaoContainingIgnoreCase(String localizacao);
    
 // Método para buscar recursos por localização (exata, respeita maiúsculas/minúsculas)
    List<Recurso> findByLocalizacao(String localizacao);

    // Consulta para verificar disponibilidade de recurso em determinado intervalo
    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.recurso = :recurso " +
           "AND ((r.startDate BETWEEN :startDate AND :endDate) " +
           "OR (r.endDate BETWEEN :startDate AND :endDate))")
    boolean isRecursoDisponivel(
        @Param("recurso") Recurso recurso, 
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );
}
