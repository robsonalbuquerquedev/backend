package br.edu.ifpe.manager.controller;

import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    @Autowired
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<Reserva> criarReserva(@RequestBody @Valid Reserva reserva) {
        // Verifique se o recursoId e usuarioId estão sendo passados corretamente
        if (reserva.getRecurso() == null || reserva.getUsuario() == null) {
            return ResponseEntity.badRequest().body(null); // Retorna um erro caso o recurso ou usuario não tenha sido informado
        }

        Reserva novaReserva = reservaService.criarReserva(reserva);
        return ResponseEntity.ok(novaReserva);
    }
}
