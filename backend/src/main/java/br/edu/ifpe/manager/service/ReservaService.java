package br.edu.ifpe.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpe.manager.dto.ReservaRequest;
import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.StatusReserva;
import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.RecursoRepository;
import br.edu.ifpe.manager.repository.ReservaRepository;
import br.edu.ifpe.manager.repository.UsuarioRepository;

import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RecursoRepository recursoRepository;

    @Transactional
    public Reserva criarReserva(ReservaRequest reservaRequest) {
        // Validações de negócio
        Usuario usuario = usuarioRepository.findById(reservaRequest.getUsuarioId())
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + reservaRequest.getUsuarioId()));

        Recurso recurso = recursoRepository.findById(reservaRequest.getRecursoId())
            .orElseThrow(() -> new IllegalArgumentException("Recurso não encontrado com o ID: " + reservaRequest.getRecursoId()));

        // Criando a reserva
        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setRecurso(recurso);
        reserva.setDataHoraInicio(reservaRequest.getDataHoraInicio());
        reserva.setDataHoraFim(reservaRequest.getDataHoraFim());
        reserva.setRecursoAdicional(reservaRequest.getRecursoAdicional());
        reserva.setStatus(StatusReserva.PENDENTE);

        return reservaRepository.save(reserva);
    }

    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    public Reserva buscarReservaPorId(Long id) {
        return reservaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com o ID: " + id));
    }

    @Transactional
    public void cancelarReserva(Long id) {
        Reserva reserva = buscarReservaPorId(id);
        reserva.setStatus(StatusReserva.CANCELADA);
        reservaRepository.save(reserva);
    }
}
