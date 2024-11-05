package br.edu.ifpe.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.manager.model.Recurso;

public interface RecursoRepository extends JpaRepository<Recurso, Long> {
}
