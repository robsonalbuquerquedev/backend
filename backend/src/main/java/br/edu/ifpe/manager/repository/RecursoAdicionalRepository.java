package br.edu.ifpe.manager.repository;

import br.edu.ifpe.manager.model.RecursoAdicional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecursoAdicionalRepository extends JpaRepository<RecursoAdicional, Long> {

	// Consulta personalizada para encontrar recursos adicionais com base no nome
	List<RecursoAdicional> findByNomeContainingIgnoreCase(String nome);

	// Consulta personalizada para encontrar recursos adicionais por quantidade maior ou igual
	List<RecursoAdicional> findByQuantidadeGreaterThanEqual(Integer quantidade);

	// Método para listar todos os recursos adicionais (sem filtro)
	List<RecursoAdicional> findAll();

	// Adicione outros métodos personalizados conforme necessário
}
