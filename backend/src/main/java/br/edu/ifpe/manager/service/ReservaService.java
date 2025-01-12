package br.edu.ifpe.manager.service;

import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.StatusReserva;
import br.edu.ifpe.manager.model.TipoUsuario;
import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.ReservaRepository;
import br.edu.ifpe.manager.repository.RecursoRepository;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private RecursoRepository recursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para criar uma nova reserva
    public Reserva criarReserva(Long usuarioId, Long recursoId, LocalDateTime dataInicio, LocalDateTime dataFinal) {
        // Verifica se o usuário e o recurso existem
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + usuarioId));
        Recurso recurso = recursoRepository.findById(recursoId)
                .orElseThrow(() -> new IllegalArgumentException("Recurso não encontrado com o ID: " + recursoId));

        // Verifica se o recurso está disponível no intervalo de tempo
        List<Recurso> recursosDisponiveis = recursoRepository.findRecursosDisponiveis(dataInicio, dataFinal);
        if (!recursosDisponiveis.contains(recurso)) {
            throw new IllegalStateException("Recurso não disponível nesse intervalo de tempo.");
        }

        // Cria a reserva
        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setRecurso(recurso);
        reserva.setDataInicio(dataInicio);
        reserva.setDataFinal(dataFinal);

        // Define o status da reserva com base no tipo de usuário
        if (usuario.getTipo().equals(TipoUsuario.ALUNO)) {
            reserva.setStatus(StatusReserva.PENDENTE); // Status para alunos deve ser PENDENTE
        } else {
            reserva.setStatus(StatusReserva.CONFIRMADA); // Status para outros tipos de usuário é CONFIRMADA
        }

        // Salva a reserva
        return reservaRepository.save(reserva);
    }

    // Método para listar todas as reservas de um usuário
    public List<Reserva> listarReservasPorUsuario(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }

    // Método para listar todas as reservas de um recurso
    public List<Reserva> listarReservasPorRecurso(Long recursoId) {
        return reservaRepository.findByRecursoId(recursoId);
    }

    // Método para listar as reservas dentro de um intervalo de tempo
    public List<Reserva> listarReservasPorIntervalo(LocalDateTime dataInicio, LocalDateTime dataFinal) {
        return reservaRepository.findReservasPorIntervalo(dataInicio, dataFinal);
    }

    // Método para listar reservas pendentes (por exemplo, aguardando aprovação)
    public List<Reserva> listarReservasPendentes() {
        return reservaRepository.findReservasPendentes();
    }

    // Método para alterar o status de uma reserva
    public Reserva alterarStatus(Long reservaId, StatusReserva novoStatus) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com o ID: " + reservaId));

        reserva.setStatus(novoStatus);
        return reservaRepository.save(reserva);
    }

    // Método para excluir uma reserva
    public void excluirReserva(Long reservaId) {
        reservaRepository.deleteById(reservaId);
    }
}
