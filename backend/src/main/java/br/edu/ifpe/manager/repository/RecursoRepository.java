package br.edu.ifpe.manager.repository;

import br.edu.ifpe.manager.model.Recurso;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long> {
    // Método para buscar recursos pelo nome (contém, ignorando maiúsculas/minúsculas)
    List<Recurso> findByNomeContainingIgnoreCase(String nome);

    // Método para buscar recursos por localização (parcial, ignora maiúsculas/minúsculas)
    List<Recurso> findByLocalizacaoContainingIgnoreCase(String localizacao);

    // Método para buscar recursos por localização (exata, respeita maiúsculas/minúsculas)
    List<Recurso> findByLocalizacao(String localizacao);
    
 // Busca recursos disponíveis em um intervalo de tempo
    @Query("""
           SELECT r FROM Recurso r 
           WHERE r.id NOT IN (
               SELECT res.recurso.id FROM Reserva res 
               WHERE (res.dataInicio <= :dataFinal AND res.dataFinal >= :dataInicio)
           )
           """)
    List<Recurso> findRecursosDisponiveis(@Param("dataInicio") LocalDateTime dataInicio, 
                                          @Param("dataFinal") LocalDateTime dataFinal);
}
