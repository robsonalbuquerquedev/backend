package br.edu.ifpe.manager.controller;

import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.ReservaRequest;
import br.edu.ifpe.manager.model.StatusReserva;
import br.edu.ifpe.manager.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

	@Autowired
	private ReservaService reservaService;

	// Endpoint para criar uma nova reserva
	@PostMapping
	public ResponseEntity<Reserva> criarReserva(@RequestBody @Valid ReservaRequest reservaRequest) {
		System.out.println("Corpo da requisição recebido: " + reservaRequest);
		try {
			LocalDateTime dataInicio = reservaRequest.getDataInicio();
			LocalDateTime dataFinal = reservaRequest.getDataFinal();

			Reserva reserva = reservaService.criarReserva(
					reservaRequest.getUsuarioId(), 
					reservaRequest.getRecursoId(), 
					dataInicio, 
					dataFinal
					);
			return new ResponseEntity<>(reserva, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	// Endpoint para listar todas as reservas de um usuário
	@GetMapping("/usuario/{usuarioId}")
	public ResponseEntity<List<Reserva>> listarReservasPorUsuario(@PathVariable Long usuarioId) {
		List<Reserva> reservas = reservaService.listarReservasPorUsuario(usuarioId);
		return new ResponseEntity<>(reservas, HttpStatus.OK);
	}

	// Endpoint para listar todas as reservas de um recurso
	@GetMapping("/recurso/{recursoId}")
	public ResponseEntity<List<Reserva>> listarReservasPorRecurso(@PathVariable Long recursoId) {
		List<Reserva> reservas = reservaService.listarReservasPorRecurso(recursoId);
		return new ResponseEntity<>(reservas, HttpStatus.OK);
	}

	// Endpoint para listar reservas dentro de um intervalo de tempo
	@GetMapping("/intervalo")
	public ResponseEntity<List<Reserva>> listarReservasPorIntervalo(
			@RequestParam String dataInicio, 
			@RequestParam String dataFinal) {
		try {
			LocalDateTime dataInicioLocal = LocalDateTime.parse(dataInicio);
			LocalDateTime dataFinalLocal = LocalDateTime.parse(dataFinal);

			List<Reserva> reservas = reservaService.listarReservasPorIntervalo(dataInicioLocal, dataFinalLocal);
			return new ResponseEntity<>(reservas, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // Retorna erro se houver algum problema
		}
	}

	// Endpoint para listar todas as reservas pendentes
	@GetMapping("/pendentes")
	public ResponseEntity<List<Reserva>> listarReservasPendentes() {
		List<Reserva> reservas = reservaService.listarReservasPendentes();
		return new ResponseEntity<>(reservas, HttpStatus.OK);
	}

	// Endpoint para alterar o status de uma reserva
	@PutMapping("/{reservaId}/status")
	public ResponseEntity<Reserva> alterarStatus(
			@PathVariable Long reservaId, 
			@RequestParam StatusReserva novoStatus) {
		try {
			Reserva reserva = reservaService.alterarStatus(reservaId, novoStatus);
			return new ResponseEntity<>(reserva, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // Retorna erro se houver algum problema
		}
	}

	// Endpoint para excluir uma reserva
	@DeleteMapping("/{reservaId}")
	public ResponseEntity<Void> excluirReserva(@PathVariable Long reservaId) {
		reservaService.excluirReserva(reservaId);
		return ResponseEntity.noContent().build();
	}
}
