package br.edu.ifpe.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifpe.manager.model.Laboratorio;
import br.edu.ifpe.manager.model.StatusLaboratorio;
import br.edu.ifpe.manager.service.LaboratorioService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/laboratorios")
public class LaboratorioController {

    @Autowired
    private LaboratorioService laboratorioService;

    @GetMapping
    public ResponseEntity<List<Laboratorio>> listarTodos() {
        List<Laboratorio> laboratorios = laboratorioService.listarTodos();
        return ResponseEntity.ok(laboratorios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Laboratorio> buscarPorId(@PathVariable Long id) {
        Optional<Laboratorio> laboratorio = laboratorioService.buscarPorId(id);
        return laboratorio.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Laboratorio>> listarPorStatus(@PathVariable StatusLaboratorio status) {
        List<Laboratorio> laboratorios = laboratorioService.listarPorStatus(status);
        return ResponseEntity.ok(laboratorios);
    }

    @PostMapping
    public ResponseEntity<Laboratorio> salvar(@RequestBody Laboratorio laboratorio) {
        Laboratorio novoLaboratorio = laboratorioService.salvar(laboratorio);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoLaboratorio);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Laboratorio> atualizar(@PathVariable Long id, @RequestBody Laboratorio laboratorioAtualizado) {
        Optional<Laboratorio> laboratorioExistente = laboratorioService.buscarPorId(id);
        if (!laboratorioExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        laboratorioAtualizado.setId(id); // Defina o ID do laboratório atualizado
        Laboratorio laboratorioSalvo = laboratorioService.salvar(laboratorioAtualizado);
        return ResponseEntity.ok(laboratorioSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        laboratorioService.deletarPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
