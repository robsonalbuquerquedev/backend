package br.edu.ifpe.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.manager.model.Laboratorio;
import br.edu.ifpe.manager.model.StatusLaboratorio;

import java.util.List;

public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {
    List<Laboratorio> findByStatus(StatusLaboratorio status);
}
