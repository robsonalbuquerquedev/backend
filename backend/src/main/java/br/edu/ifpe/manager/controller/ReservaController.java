package br.edu.ifpe.manager.controller;

import br.edu.ifpe.manager.dto.ReservaRequest;
import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.RecursoRepository;
import br.edu.ifpe.manager.repository.ReservaRepository;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import br.edu.ifpe.manager.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;
    private final RecursoRepository recursoRepository;

    // Injeção das dependências
    public ReservaController(ReservaService reservaService, UsuarioRepository usuarioRepository, ReservaRepository reservaRepository, RecursoRepository recursoRepository) {
        this.reservaService = reservaService;
        this.usuarioRepository = usuarioRepository;
        this.reservaRepository = reservaRepository;
        this.recursoRepository = recursoRepository;
    }

    @PostMapping
    public ResponseEntity<Reserva> criarReserva(@RequestBody @Valid ReservaRequest reservaRequest) {
        // Buscar o usuário logado (baseado no seu código anterior)
        Long usuarioId = reservaRequest.getUsuarioId();
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
        
        if (!usuarioOptional.isPresent()) {
            return ResponseEntity.badRequest().body(null);  // Usuário não encontrado
        }

        Usuario usuario = usuarioOptional.get();
        
        // Buscar o recurso associado ao recursoId
        Optional<Recurso> recursoOptional = recursoRepository.findById(reservaRequest.getRecursoId());
        
        if (!recursoOptional.isPresent()) {
            return ResponseEntity.badRequest().body(null);  // Recurso não encontrado
        }

        Recurso recurso = recursoOptional.get();
        
        // Verificar se a data de início não é no passado
        if (reservaRequest.getStartDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(null); // Data de início não pode ser no passado
        }

        // Verificar conflito de reserva com o recurso encontrado
        if (reservaRepository.existsReservaInPeriod(recurso, reservaRequest.getStartDate(), reservaRequest.getEndDate())) {
            return ResponseEntity.badRequest().body(null); // Conflito de reserva
        }

        // Criar a reserva
        Reserva reserva = new Reserva();
        reserva.setStartDate(reservaRequest.getStartDate());
        reserva.setEndDate(reservaRequest.getEndDate());
        reserva.setIncludeAdditionalResource(reservaRequest.isIncludeAdditionalResource());
        reserva.setAdditionalResource(reservaRequest.getAdditionalResource());
        reserva.setRecurso(recurso); // Atribuindo o recurso encontrado
        reserva.setUsuario(usuario); // Atribuindo o usuário logado

        // Salvar no banco
        reservaService.criarReserva(reservaRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id) {
        try {
            reservaService.cancelarReserva(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> listarReservas() {
        List<Reserva> reservas = reservaService.listarReservas();
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }
}
