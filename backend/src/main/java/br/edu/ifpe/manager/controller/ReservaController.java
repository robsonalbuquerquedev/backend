package br.edu.ifpe.manager.controller;

import br.edu.ifpe.manager.dto.ReservaDTO;
import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.request.ReservaRequest;
import br.edu.ifpe.manager.service.ReservaService;

import org.springframework.http.HttpStatus;
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

	// Método para listar todas as reservas e retornar uma lista de ReservaDTO
	@GetMapping
	public List<ReservaDTO> listarReservas() {
		return reservaService.listarTodas(); // Retorna uma lista de ReservaDTO
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<Reserva>> buscarReservasPorStatus(@PathVariable String status) {
		try {
			List<Reserva> reservas = reservaService.buscarReservasPorStatus(status);
			return ResponseEntity.ok(reservas);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// Método para criar uma nova reserva e retornar um ResponseEntity com ReservaDTO
	@PostMapping
	public ResponseEntity<?> criarReserva(@RequestBody ReservaRequest request) {
		try {
			ReservaDTO reservaDTO = reservaService.criarReserva(request); // Cria a reserva e retorna ReservaDTO
			return ResponseEntity.ok(reservaDTO); // Retorna a ReservaDTO na resposta
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Erro ao criar reserva: " + e.getMessage());
		}
	}

	@PostMapping("/{id}/cancelar")
	public ResponseEntity<?> cancelarReserva(@PathVariable Long id) {
		try {
			reservaService.cancelarReserva(id);
			return ResponseEntity.ok().body("Reserva cancelada com sucesso.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cancelar reserva.");
		}
	}
	
	@PostMapping("/approveReserva/{id}")
	public ResponseEntity<?> aprovarReserva(@PathVariable Long id) {
	    try {
	        reservaService.aprovarReserva(id);
	        return ResponseEntity.ok("Reserva aprovada com sucesso.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Erro ao aprovar reserva: " + e.getMessage());
	    }
	}

	@PostMapping("/rejectReserva/{id}")
	public ResponseEntity<?> rejeitarReserva(@PathVariable Long id) {
	    try {
	        reservaService.rejeitarReserva(id);
	        return ResponseEntity.ok("Reserva rejeitada com sucesso.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Erro ao rejeitar reserva: " + e.getMessage());
	    }
	}

	/*@PostMapping("/approveReserva/{id}")
	public ResponseEntity<?> aprovarReserva(@PathVariable Long id, @RequestParam Boolean aprovado) {
		try {
			// Aprova ou rejeita a reserva com base no parâmetro "aprovado"
			reservaService.aprovarOuRejeitarReserva(id, aprovado);
			String status = aprovado ? "aprovada" : "rejeitada";
			return ResponseEntity.ok("Reserva " + status + " com sucesso.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao aprovar ou rejeitar reserva: " + e.getMessage());
		}
	}*/
}
