package br.edu.ifpe.manager.service;

import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    // Método para salvar ou atualizar uma reserva
    public Reserva salvarReserva(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    // Método para buscar uma reserva por ID
    public Optional<Reserva> buscarReservaPorId(Long id) {
        return reservaRepository.findById(id);
    }

    // Método para listar todas as reservas
    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    // Método para listar reservas de um usuário específico
    public List<Reserva> listarReservasPorUsuario(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }

    // Método para listar reservas de um recurso específico
    public List<Reserva> listarReservasPorRecurso(Long recursoId) {
        return reservaRepository.findByRecursoId(recursoId);
    }

    // Método para verificar conflitos de reservas para um recurso
    public List<Reserva> verificarConflitoDeReserva(Long recursoId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return reservaRepository.findByRecursoIdAndDataInicioBeforeAndDataFimAfter(recursoId, dataFim, dataInicio);
    }

    // Método para excluir uma reserva
    public void excluirReserva(Long id) {
        reservaRepository.deleteById(id);
    }
}
