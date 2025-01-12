package br.edu.ifpe.manager.controller;

import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.service.RecursoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recursos")
public class RecursoController {

    @Autowired
    private RecursoService recursoService;

    // Endpoint para listar todos os recursos
    @GetMapping
    public ResponseEntity<List<Recurso>> listarRecursos() {
        List<Recurso> recursos = recursoService.listarRecursos();
        return new ResponseEntity<>(recursos, HttpStatus.OK);
    }

    // Endpoint para buscar recursos por nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Recurso>> buscarRecursosPorNome(@PathVariable String nome) {
        List<Recurso> recursos = recursoService.buscarRecursosPorNome(nome);
        return new ResponseEntity<>(recursos, HttpStatus.OK);
    }

    // Endpoint para buscar recursos por localização exata
    @GetMapping("/localizacao/{localizacao}")
    public ResponseEntity<List<Recurso>> buscarRecursosPorLocalizacao(@PathVariable String localizacao) {
        List<Recurso> recursos = recursoService.buscarRecursosPorLocalizacao(localizacao);
        return new ResponseEntity<>(recursos, HttpStatus.OK);
    }

    // Endpoint para buscar recursos por localização (contém)
    @GetMapping("/localizacao/contendo/{localizacao}")
    public ResponseEntity<List<Recurso>> buscarRecursosPorLocalizacaoParcial(@PathVariable String localizacao) {
        List<Recurso> recursos = recursoService.buscarRecursosPorLocalizacaoParcial(localizacao);
        return new ResponseEntity<>(recursos, HttpStatus.OK);
    }

    // Endpoint para salvar ou atualizar um recurso
    @PostMapping
    public ResponseEntity<Recurso> salvarRecurso(@RequestBody @Valid Recurso recurso) {
        Recurso recursoSalvo = recursoService.salvarRecurso(recurso);
        return new ResponseEntity<>(recursoSalvo, HttpStatus.CREATED);
    }

    // Endpoint para atualizar um recurso
    @PutMapping("/{id}")
    public ResponseEntity<Recurso> atualizarRecurso(@PathVariable Long id, @RequestBody Recurso recurso) {
        recurso.setId(id);
        Recurso recursoAtualizado = recursoService.salvarRecurso(recurso);
        return ResponseEntity.ok(recursoAtualizado);
    }

    // Endpoint para excluir um recurso
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirRecurso(@PathVariable Long id) {
        recursoService.excluirRecurso(id);
        return ResponseEntity.noContent().build();
    }
}
