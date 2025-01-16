package br.edu.ifpe.manager.controller;

import br.edu.ifpe.manager.dto.ReservaDTO;
import br.edu.ifpe.manager.model.StatusReserva;
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

	// Método para atualizar uma reserva e retornar um ResponseEntity com ReservaDTO
	@PutMapping("/{id}")
	public ResponseEntity<ReservaDTO> atualizarReserva(@PathVariable Long id, @RequestBody ReservaRequest request) {
		ReservaDTO reservaAtualizada = reservaService.atualizarReserva(id, request); // Atualiza a reserva
		return ResponseEntity.ok(reservaAtualizada); // Retorna a ReservaDTO na resposta
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

	// Método para alterar o status de uma reserva
	@PatchMapping("/{id}/status")
	public ResponseEntity<?> alterarStatus(@PathVariable Long id,
			@RequestParam StatusReserva novoStatus,
			@RequestParam Long usuarioId) {
		try {
			// Chama o serviço para alterar o status
			ReservaDTO reservaAtualizada = reservaService.alterarStatus(id, novoStatus, usuarioId);
			return ResponseEntity.ok(reservaAtualizada);  // Retorna a resposta com o DTO atualizado
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(400).body("Erro ao alterar status: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Erro inesperado ao alterar status: " + e.getMessage());
		}
	}

	// Método para deletar uma reserva
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletarReserva(@PathVariable Long id) {
		reservaService.deletarReserva(id); // Deleta a reserva
		return ResponseEntity.noContent().build(); // Retorna 204 No Content
	}
}
