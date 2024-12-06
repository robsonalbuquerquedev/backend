package br.edu.ifpe.manager.controller;

import br.edu.ifpe.manager.dto.ReservaDTO;
import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.StatusRecurso;
import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.service.RecursoService;
import br.edu.ifpe.manager.service.ReservaService;
import br.edu.ifpe.manager.service.UsuarioService;
import jakarta.validation.Valid;

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
    
    @Autowired
    private UsuarioService usuarioService;  // Injeção do UsuarioService

    @Autowired
    private RecursoService recursoService;  // Injeção do RecursoService

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

 // Endpoint para salvar a reserva
    @PostMapping
    public ResponseEntity<Reserva> salvarReserva(@RequestBody @Valid ReservaDTO reservaDTO) {
        try {
            // Validando as datas da reserva
            if (reservaDTO.getDataInicio().isAfter(reservaDTO.getDataFim())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // Buscando o usuário e o recurso a partir dos IDs fornecidos
            Usuario usuario = usuarioService.buscarUsuarioPorId(reservaDTO.getUsuarioId());
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Usuário não encontrado
            }

            Recurso recurso = recursoService.buscarRecursoPorId(reservaDTO.getRecursoId());
            if (recurso == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Recurso não encontrado
            }

            // Verificando se o recurso está disponível
            if (recurso.getStatus() != StatusRecurso.DISPONIVEL) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null);  // Recurso não está disponível para reserva
            }

            // Criando a reserva
            Reserva reserva = new Reserva();
            reserva.setDataInicio(reservaDTO.getDataInicio());
            reserva.setDataFim(reservaDTO.getDataFim());
            reserva.setRecursoAdicional(reservaDTO.getRecursoAdicional());
            reserva.setUsuario(usuario);
            reserva.setRecurso(recurso);

            // Salvando a reserva
            Reserva reservaSalva = reservaService.salvarReserva(reserva);

            // Atualizando o status do recurso para "RESERVADO"
            recurso.setStatus(StatusRecurso.RESERVADO);
            recursoService.salvarRecurso(recurso);

            return new ResponseEntity<>(reservaSalva, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);  // Erro inesperado
        }
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
