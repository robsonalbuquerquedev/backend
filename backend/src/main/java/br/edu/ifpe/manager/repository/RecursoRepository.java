package br.edu.ifpe.manager.repository;

import br.edu.ifpe.manager.model.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long> {
    
    // Busca recursos pelo nome (parcial, ignorando maiúsculas/minúsculas)
    List<Recurso> findByNomeContainingIgnoreCase(String nome);

    // Busca recursos por localização (parcial, ignorando maiúsculas/minúsculas)
    List<Recurso> findByLocalizacaoContainingIgnoreCase(String localizacao);
    
 // Método para buscar recursos por localização (exata, respeita maiúsculas/minúsculas)
    List<Recurso> findByLocalizacao(String localizacao);
}
