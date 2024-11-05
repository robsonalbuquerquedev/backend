package br.edu.ifpe.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.service.ReservaService;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<String> criarReserva(@RequestBody Reserva novaReserva) {
        try {
            reservaService.criarReserva(novaReserva);
            return ResponseEntity.ok("Reserva criada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
