package br.edu.ifpe.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.StatusRecurso;
import br.edu.ifpe.manager.repository.RecursoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecursoService {

	@Autowired
	private RecursoRepository recursoRepository;

	// Método para listar todos os recursos (salas e laboratórios)
	public List<Recurso> listarTodos() {
		return recursoRepository.findAll();
	}

	// Listar recursos por status
	public List<Recurso> listarPorStatus(StatusRecurso status) {
		return recursoRepository.findByStatus(status);
	}

	// Método para buscar recurso por ID (pode ser sala ou laboratório)
	public Optional<Recurso> buscarPorId(Long id) {
		return recursoRepository.findById(id);
	}

	// Método para salvar ou atualizar um recurso (sala ou laboratório)
	// Método para salvar o recurso
	public Recurso salvarRecurso(Recurso recurso) {
		return recursoRepository.save(recurso);
	}

	// Método para deletar recurso por ID (pode ser sala ou laboratório)
	public void deletarPorId(Long id) {
		recursoRepository.deleteById(id);
	}
}