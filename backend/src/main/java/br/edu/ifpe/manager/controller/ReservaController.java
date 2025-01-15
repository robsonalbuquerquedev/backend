package br.edu.ifpe.manager.controller;

import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.request.ReservaRequest;
import br.edu.ifpe.manager.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

	private final ReservaService reservaService;

	public ReservaController(ReservaService reservaService) {
		this.reservaService = reservaService;
	}

	@GetMapping
	public List<Reserva> listarReservas() {
		return reservaService.listarTodas();
	}

	@PostMapping
	public ResponseEntity<?> criarReserva(@RequestBody ReservaRequest request) {
	    try {
	        Reserva reserva = reservaService.criarReserva(request);
	        return ResponseEntity.ok(reserva);
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Erro ao criar reserva: " + e.getMessage());
	    }
	}

	@PutMapping("/{id}")
	public ResponseEntity<Reserva> atualizarReserva(@PathVariable Long id, @RequestBody ReservaRequest request) {
		Reserva reservaAtualizada = reservaService.atualizarReserva(id, request);
		return ResponseEntity.ok(reservaAtualizada);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletarReserva(@PathVariable Long id) {
		reservaService.deletarReserva(id);
		return ResponseEntity.noContent().build();
	}
}
