package br.edu.ifpe.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifpe.manager.dto.RecursoAdicionalDTO;
import br.edu.ifpe.manager.service.RecursoAdicionalService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recurso-adicional")
public class RecursoAdicionalController {

    @Autowired
    private RecursoAdicionalService recursoAdicionalService;

    // Listar todos os recursos adicionais
    @GetMapping
    public ResponseEntity<List<RecursoAdicionalDTO>> listarTodos() {
        List<RecursoAdicionalDTO> recursosAdicionais = recursoAdicionalService.listarTodos();
        return ResponseEntity.ok(recursosAdicionais);
    }

    // Buscar recurso adicional por ID
    @GetMapping("/{id}")
    public ResponseEntity<RecursoAdicionalDTO> buscarPorId(@PathVariable Long id) {
        Optional<RecursoAdicionalDTO> recursoAdicionalDTO = recursoAdicionalService.buscarPorId(id);
        return recursoAdicionalDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Criar novo recurso adicional
    @PostMapping
    public ResponseEntity<RecursoAdicionalDTO> salvar(@Valid @RequestBody RecursoAdicionalDTO recursoAdicionalDTO) {
        RecursoAdicionalDTO novoRecurso = recursoAdicionalService.salvar(recursoAdicionalDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoRecurso);
    }

    // Deletar recurso adicional por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        recursoAdicionalService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
