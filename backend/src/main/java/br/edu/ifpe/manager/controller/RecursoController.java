package br.edu.ifpe.manager.controller;

import br.edu.ifpe.manager.dto.RecursoDTO;
import br.edu.ifpe.manager.request.RecursoRequest;
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

    // Endpoint para listar todos os recursos como DTOs
    @GetMapping
    public ResponseEntity<List<RecursoDTO>> listarRecursos() {
        List<RecursoDTO> recursos = recursoService.listarRecursos();
        return new ResponseEntity<>(recursos, HttpStatus.OK);
    }

    // Endpoint para buscar recursos por nome como DTOs
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<RecursoDTO>> buscarRecursosPorNome(@PathVariable String nome) {
        List<RecursoDTO> recursos = recursoService.buscarRecursosPorNome(nome);
        return new ResponseEntity<>(recursos, HttpStatus.OK);
    }

    // Endpoint para buscar recursos por localização exata como DTOs
    @GetMapping("/localizacao/{localizacao}")
    public ResponseEntity<List<RecursoDTO>> buscarRecursosPorLocalizacao(@PathVariable String localizacao) {
        List<RecursoDTO> recursos = recursoService.buscarRecursosPorLocalizacao(localizacao);
        return new ResponseEntity<>(recursos, HttpStatus.OK);
    }

    // Endpoint para buscar recursos por localização parcial como DTOs
    @GetMapping("/localizacao/contendo/{localizacao}")
    public ResponseEntity<List<RecursoDTO>> buscarRecursosPorLocalizacaoParcial(@PathVariable String localizacao) {
        List<RecursoDTO> recursos = recursoService.buscarRecursosPorLocalizacaoParcial(localizacao);
        return new ResponseEntity<>(recursos, HttpStatus.OK);
    }
    
    // Endpoint para salvar um recurso e retornar como DTO
    @PostMapping
    public ResponseEntity<RecursoDTO> salvarRecurso(@RequestBody @Valid RecursoRequest recursoRequest) {
        RecursoDTO recursoSalvo = recursoService.salvarRecurso(recursoRequest);
        return new ResponseEntity<>(recursoSalvo, HttpStatus.CREATED);
    }

    // Endpoint para atualizar um recurso e retornar como DTO
    @PutMapping("/{id}")
    public ResponseEntity<RecursoDTO> atualizarRecurso(@PathVariable Long id, @RequestBody @Valid RecursoRequest recursoRequest) {
        // Atualizando o ID no Request antes de salvar
        recursoRequest.setId(id);
        RecursoDTO recursoAtualizado = recursoService.salvarRecurso(recursoRequest);
        return ResponseEntity.ok(recursoAtualizado);
    }

    // Endpoint para excluir um recurso
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirRecurso(@PathVariable Long id) {
        recursoService.excluirRecurso(id);
        return ResponseEntity.noContent().build();
    }
}
