package br.edu.ifpe.manager.controller;

import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // Endpoint para listar todas as reservas
    @GetMapping
    public ResponseEntity<List<Reserva>> listarReservas() {
        List<Reserva> reservas = reservaService.listarReservas();
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }

    // Endpoint para buscar reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscarReservaPorId(@PathVariable Long id) {
        Optional<Reserva> reserva = reservaService.buscarReservaPorId(id);
        return reserva.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para salvar ou atualizar uma reserva
    @PostMapping
    public ResponseEntity<Reserva> salvarReserva(@RequestBody Reserva reserva) {
        Reserva reservaSalva = reservaService.salvarReserva(reserva);
        return new ResponseEntity<>(reservaSalva, HttpStatus.CREATED);
    }

    // Endpoint para atualizar uma reserva
    @PutMapping("/{id}")
    public ResponseEntity<Reserva> atualizarReserva(@PathVariable Long id, @RequestBody Reserva reserva) {
        reserva.setId(id);
        Reserva reservaAtualizada = reservaService.salvarReserva(reserva);
        return ResponseEntity.ok(reservaAtualizada);
    }

    // Endpoint para excluir uma reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirReserva(@PathVariable Long id) {
        reservaService.excluirReserva(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para verificar se há conflito de reservas
    @GetMapping("/conflito")
    public ResponseEntity<List<Reserva>> verificarConflitoDeReserva(
            @RequestParam Long recursoId,
            @RequestParam LocalDateTime dataInicio,
            @RequestParam LocalDateTime dataFim) {

        List<Reserva> reservasConflito = reservaService.verificarConflitoDeReserva(recursoId, dataInicio, dataFim);
        return new ResponseEntity<>(reservasConflito, HttpStatus.OK);
    }

    // Endpoint para listar reservas por recurso adicional (se aplicável)
    @GetMapping("/recursoAdicional")
    public ResponseEntity<List<Reserva>> listarReservasPorRecursoAdicional(
            @RequestParam String recursoAdicional) {
        
        List<Reserva> reservas = reservaService.listarReservasPorRecursoAdicional(recursoAdicional);
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }
}
