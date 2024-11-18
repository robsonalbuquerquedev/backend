package br.edu.ifpe.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifpe.manager.model.RecursoAdicional;

public interface RecursoAdicionalRepository extends JpaRepository<RecursoAdicional, Long> {
    // Métodos personalizados podem ser adicionados aqui, caso necessário
}
