package br.edu.ifpe.manager.controller;

import br.edu.ifpe.manager.model.RecursoAdicional;
import br.edu.ifpe.manager.service.RecursoAdicionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recursos-adicionais")
public class RecursoAdicionalController {

    @Autowired
    private RecursoAdicionalService recursoAdicionalService;

    // Endpoint para listar todos os recursos adicionais
    @GetMapping
    public ResponseEntity<List<RecursoAdicional>> listarRecursosAdicionais() {
        List<RecursoAdicional> recursosAdicionais = recursoAdicionalService.listarRecursosAdicionais();
        return new ResponseEntity<>(recursosAdicionais, HttpStatus.OK);
    }

    // Endpoint para buscar recurso adicional por ID
    @GetMapping("/{id}")
    public ResponseEntity<RecursoAdicional> buscarRecursoAdicionalPorId(@PathVariable Long id) {
        Optional<RecursoAdicional> recursoAdicional = recursoAdicionalService.buscarRecursoAdicionalPorId(id);
        return recursoAdicional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para salvar ou atualizar um recurso adicional
    @PostMapping
    public ResponseEntity<RecursoAdicional> salvarRecursoAdicional(@RequestBody RecursoAdicional recursoAdicional) {
        RecursoAdicional recursoAdicionalSalvo = recursoAdicionalService.salvarRecursoAdicional(recursoAdicional);
        return new ResponseEntity<>(recursoAdicionalSalvo, HttpStatus.CREATED);
    }

    // Endpoint para atualizar um recurso adicional
    @PutMapping("/{id}")
    public ResponseEntity<RecursoAdicional> atualizarRecursoAdicional(@PathVariable Long id, @RequestBody RecursoAdicional recursoAdicional) {
        recursoAdicional.setId(id);
        RecursoAdicional recursoAdicionalAtualizado = recursoAdicionalService.salvarRecursoAdicional(recursoAdicional);
        return ResponseEntity.ok(recursoAdicionalAtualizado);
    }

    // Endpoint para excluir um recurso adicional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirRecursoAdicional(@PathVariable Long id) {
        recursoAdicionalService.excluirRecursoAdicional(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para buscar recursos adicionais por nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<RecursoAdicional>> listarRecursosAdicionaisPorNome(@PathVariable String nome) {
        List<RecursoAdicional> recursosAdicionais = recursoAdicionalService.listarRecursosAdicionaisPorNome(nome);
        return new ResponseEntity<>(recursosAdicionais, HttpStatus.OK);
    }

    // Endpoint para buscar recursos adicionais por quantidade
    @GetMapping("/quantidade/{quantidade}")
    public ResponseEntity<List<RecursoAdicional>> listarRecursosAdicionaisPorQuantidade(@PathVariable Integer quantidade) {
        List<RecursoAdicional> recursosAdicionais = recursoAdicionalService.listarRecursosAdicionaisPorQuantidade(quantidade);
        return new ResponseEntity<>(recursosAdicionais, HttpStatus.OK);
    }
}
