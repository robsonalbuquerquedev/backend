package br.edu.ifpe.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifpe.manager.dto.RecursoAdicionalDTO;
import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.RecursoAdicional;
import br.edu.ifpe.manager.service.RecursoAdicionalService;
import br.edu.ifpe.manager.service.RecursoService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recurso-adicional")
public class RecursoAdicionalController {

    @Autowired
    private RecursoAdicionalService recursoAdicionalService;

    @Autowired
    private RecursoService recursoService; // Adicionando a injeção do serviço RecursoService

    // Listar todos os recursos adicionais
    @GetMapping
    public ResponseEntity<List<RecursoAdicional>> listarTodos() {
        List<RecursoAdicional> recursosAdicionais = recursoAdicionalService.listarTodos();
        return ResponseEntity.ok(recursosAdicionais);
    }

    // Buscar recurso adicional por ID
    @GetMapping("/{id}")
    public ResponseEntity<RecursoAdicional> buscarPorId(@PathVariable Long id) {
        Optional<RecursoAdicional> recursoAdicional = recursoAdicionalService.buscarPorId(id);
        return recursoAdicional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Criar ou atualizar recurso adicional
    @PostMapping
    public ResponseEntity<RecursoAdicional> salvar(@Valid @RequestBody RecursoAdicionalDTO recursoAdicionalDTO) {
        // Buscar o recurso pelo ID, utilizando o ID que está dentro do DTO
        Optional<Recurso> recursoOptional = recursoService.buscarPorId(recursoAdicionalDTO.getRecurso().getId());

        if (!recursoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Ou qualquer lógica de erro adequada
        }

        // Criar e salvar o recurso adicional
        RecursoAdicional recursoAdicional = new RecursoAdicional();
        recursoAdicional.setNome(recursoAdicionalDTO.getNome());
        recursoAdicional.setDescricao(recursoAdicionalDTO.getDescricao());
        recursoAdicional.setRecurso(recursoOptional.get());  // Atribui o recurso encontrado

        RecursoAdicional novoRecurso = recursoAdicionalService.salvarRecursoAdicional(recursoAdicional);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoRecurso);
    }

    // Deletar recurso adicional por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        recursoAdicionalService.deletarPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
