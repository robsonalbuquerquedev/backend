package br.edu.ifpe.manager.repository;

import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.StatusRecurso;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long> {

    // Método para buscar recursos pelo status
    List<Recurso> findByStatus(StatusRecurso status);

    // Método para buscar recursos pelo nome (contém, ignorando maiúsculas/minúsculas)
    List<Recurso> findByNomeContainingIgnoreCase(String nome);

    // Método para buscar recursos por localização (parcial, ignora maiúsculas/minúsculas)
    List<Recurso> findByLocalizacaoContainingIgnoreCase(String localizacao);

    // Método para buscar recursos por localização (exata, respeita maiúsculas/minúsculas)
    List<Recurso> findByLocalizacao(String localizacao);

    // Método para verificar a disponibilidade do recurso no intervalo de tempo
    @Query("SELECT r FROM Recurso r JOIN r.reservas res " +
           "WHERE r.id = :recursoId " +
           "AND ((res.dataInicio BETWEEN :dataInicio AND :dataFim) " +
           "OR (res.dataFim BETWEEN :dataInicio AND :dataFim) " +
           "OR (res.dataInicio <= :dataInicio AND res.dataFim >= :dataFim))")
    List<Recurso> verificarDisponibilidade(Long recursoId, LocalDateTime dataInicio, LocalDateTime dataFim);
}
