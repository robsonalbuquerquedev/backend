package br.edu.ifpe.manager.controller;


import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.StatusRecurso;
import br.edu.ifpe.manager.dto.RecursoDTO;
import br.edu.ifpe.manager.service.RecursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recursos")
public class RecursoController {

	@Autowired
	private RecursoService recursoService;

	// Listar todos os recursos (salas e laboratórios)
	@GetMapping
	public ResponseEntity<List<Recurso>> listarTodos() {
		List<Recurso> recursos = recursoService.listarTodos();
		return ResponseEntity.ok(recursos);
	}

	// Listar recursos por status
	@GetMapping("/status/{status}")
	public ResponseEntity<List<Recurso>> listarPorStatus(@PathVariable StatusRecurso status) {
	    List<Recurso> recursos = recursoService.listarPorStatus(status);
	    return ResponseEntity.ok(recursos);
	}

	// Buscar recurso por ID (pode ser sala ou laboratório)
	@GetMapping("/{id}")
    public ResponseEntity<Recurso> buscarRecursoPorId(@PathVariable Long id) {
        return recursoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

	// Salvar novo recurso (sala ou laboratório)
	@PostMapping
	public ResponseEntity<String> addRecurso(@RequestBody RecursoDTO recursoDTO) {
		try {
			// Converte DTO para a entidade Recurso
			Recurso recurso = recursoDTO.toRecurso();

			// Salva o recurso através do serviço
			recursoService.salvarRecurso(recurso);

			return ResponseEntity.status(HttpStatus.CREATED).body("Recurso criado com sucesso!");
		} catch (IllegalArgumentException e) {
			// Caso o tipo do recurso seja inválido
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> editarRecurso(@PathVariable Long id, @RequestBody RecursoDTO recursoDTO) {
		Optional<Recurso> recursoExistente = recursoService.buscarPorId(id);

		if (recursoExistente.isPresent()) {
			Recurso recurso = recursoExistente.get();
			recurso.setNome(recursoDTO.getNome());
			recurso.setDescricao(recursoDTO.getDescricao());
			recurso.setCapacidade(recursoDTO.getCapacidade());
			recurso.setStatus(recursoDTO.getStatus());

			recursoService.salvarRecurso(recurso);

			return ResponseEntity.ok("Recurso atualizado com sucesso!");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recurso não encontrado!");
		}
	}

	// Deletar recurso por ID (pode ser sala ou laboratório)
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
		recursoService.deletarPorId(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
