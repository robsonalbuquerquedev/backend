package br.edu.ifpe.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifpe.manager.dto.ReservaRequest;
import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.service.ReservaService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<Reserva> criarReserva(@RequestBody @Valid ReservaRequest reservaRequest) {
        Reserva reserva = reservaService.criarReserva(reservaRequest);
        return ResponseEntity.ok(reserva);
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> listarReservas() {
        return ResponseEntity.ok(reservaService.listarReservas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscarReservaPorId(@PathVariable Long id) {
        Reserva reserva = reservaService.buscarReservaPorId(id);
        return ResponseEntity.ok(reserva);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }
}
