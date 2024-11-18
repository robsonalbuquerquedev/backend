package br.edu.ifpe.manager.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.service.ReservaService;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // Método para criar reserva
    @PostMapping
    public ResponseEntity<String> criarReserva(@RequestBody Reserva novaReserva) {
        try {
            reservaService.criarReserva(novaReserva);
            return ResponseEntity.status(HttpStatus.CREATED).body("Reserva criada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Método para listar todas as reservas
    @GetMapping
    public ResponseEntity<List<Reserva>> listarReservas() {
        List<Reserva> reservas = reservaService.listarReservas();
        return ResponseEntity.ok(reservas);
    }

    // Método para buscar reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscarReservaPorId(@PathVariable Long id) {
        Optional<Reserva> reserva = reservaService.buscarReservaPorId(id);
        return reserva.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Método para excluir reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirReserva(@PathVariable Long id) {
        reservaService.excluirReserva(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
