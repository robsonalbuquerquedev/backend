package br.edu.ifpe.manager.controller;

import br.edu.ifpe.manager.dto.ReservaDTO;
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

    // Método para listar todas as reservas e retornar uma lista de ReservaDTO
    @GetMapping
    public List<ReservaDTO> listarReservas() {
        return reservaService.listarTodas();  // Agora retorna uma lista de ReservaDTO
    }

    // Método para criar uma nova reserva e retornar um ResponseEntity com ReservaDTO
    @PostMapping
    public ResponseEntity<?> criarReserva(@RequestBody ReservaRequest request) {
        try {
            ReservaDTO reservaDTO = reservaService.criarReserva(request);  // Retorna ReservaDTO
            return ResponseEntity.ok(reservaDTO);  // Retorna a ReservaDTO na resposta
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao criar reserva: " + e.getMessage());
        }
    }

    // Método para atualizar uma reserva e retornar um ResponseEntity com ReservaDTO
    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO> atualizarReserva(@PathVariable Long id, @RequestBody ReservaRequest request) {
        ReservaDTO reservaAtualizada = reservaService.atualizarReserva(id, request);  // Retorna ReservaDTO
        return ResponseEntity.ok(reservaAtualizada);  // Retorna a ReservaDTO na resposta
    }

    // Método para deletar uma reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarReserva(@PathVariable Long id) {
        reservaService.deletarReserva(id);
        return ResponseEntity.noContent().build();  // Não há corpo na resposta
    }
}
