package br.edu.ifpe.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.StatusRecurso;

import java.util.List;

public interface RecursoRepository extends JpaRepository<Recurso, Long> {
	// Método para listar todos os recursos
	List<Recurso> findAll();  // Já fornecido pelo JpaRepository

	// Busca por status
	List<Recurso> findByStatus(StatusRecurso status);
}