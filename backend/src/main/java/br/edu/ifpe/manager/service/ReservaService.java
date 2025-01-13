package br.edu.ifpe.manager.service;

import br.edu.ifpe.manager.dto.ReservaRequest;
import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.ReservaRepository;
import br.edu.ifpe.manager.repository.RecursoRepository;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final RecursoRepository recursoRepository;
    private final UsuarioRepository usuarioRepository;

    public ReservaService(ReservaRepository reservaRepository,
                          RecursoRepository recursoRepository,
                          UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.recursoRepository = recursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Reserva criarReserva(ReservaRequest reservaRequest) {
        // Verifica se o recurso existe
        Recurso recurso = recursoRepository.findById(reservaRequest.getRecursoId())
                .orElseThrow(() -> new IllegalArgumentException("Recurso não encontrado"));

        // Verifica se o usuário existe
        Usuario usuario = usuarioRepository.findById(reservaRequest.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));       

        // Cria a nova reserva
        Reserva reserva = new Reserva();
        reserva.setRecurso(recurso);
        reserva.setUsuario(usuario);
        reserva.setStartDate(reservaRequest.getStartDate());
        reserva.setEndDate(reservaRequest.getEndDate());
        reserva.setIncludeAdditionalResource(reservaRequest.isIncludeAdditionalResource());
        reserva.setAdditionalResource(reservaRequest.getAdditionalResource());

        // Salva a reserva no banco
        return reservaRepository.save(reserva);
    }

    public void cancelarReserva(Long reservaId) {
        // Localiza a reserva
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada"));
        
        // Remove a reserva
        reservaRepository.delete(reserva);
    }
    
    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }
}
