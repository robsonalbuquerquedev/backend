package br.edu.ifpe.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifpe.manager.model.Sala;
import br.edu.ifpe.manager.model.StatusSala;
import br.edu.ifpe.manager.service.SalaService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/salas")
public class SalaController {

    @Autowired
    private SalaService salaService;

    @GetMapping
    public ResponseEntity<List<Sala>> listarTodos() {
        List<Sala> salas = salaService.listarTodos();
        return ResponseEntity.ok(salas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sala> buscarPorId(@PathVariable Long id) {
        Optional<Sala> sala = salaService.buscarPorId(id);
        return sala.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Sala>> listarPorStatus(@PathVariable StatusSala status) {
        List<Sala> salas = salaService.listarPorStatus(status);
        return ResponseEntity.ok(salas);
    }

    @PostMapping
    public ResponseEntity<Sala> salvar(@RequestBody Sala sala) {
        Sala novaSala = salaService.salvar(sala);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaSala);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Sala> atualizar(@PathVariable Long id, @RequestBody Sala salaAtualizada) {
        Optional<Sala> salaExistente = salaService.buscarPorId(id);
        if (!salaExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        salaAtualizada.setId(id); // Define o ID da sala atualizada
        Sala salaSalva = salaService.salvar(salaAtualizada);
        return ResponseEntity.ok(salaSalva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        salaService.deletarPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
