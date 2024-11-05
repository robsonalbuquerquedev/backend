package br.edu.ifpe.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.service.RecursoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recursos")
public class RecursoController {

    @Autowired
    private RecursoService recursoService;

    @GetMapping
    public ResponseEntity<List<Recurso>> listarTodos() {
        List<Recurso> recursos = recursoService.listarTodos();
        return ResponseEntity.ok(recursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recurso> buscarPorId(@PathVariable Long id) {
        Optional<Recurso> recurso = recursoService.buscarPorId(id);
        return recurso.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Recurso> salvar(@RequestBody Recurso recurso) {
        Recurso novoRecurso = recursoService.salvar(recurso);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoRecurso);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        recursoService.deletarPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
