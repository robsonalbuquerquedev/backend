package br.edu.ifpe.manager.service;

import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.StatusReserva;
import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.dto.ReservaDTO;
import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.repository.ReservaRepository;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import br.edu.ifpe.manager.request.ReservaRequest;
import br.edu.ifpe.manager.repository.RecursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

	private final ReservaRepository reservaRepository;
	private final UsuarioRepository usuarioRepository;
	private final RecursoRepository recursoRepository;

	public ReservaService(ReservaRepository reservaRepository, 
			UsuarioRepository usuarioRepository,
			RecursoRepository recursoRepository) {
		this.reservaRepository = reservaRepository;
		this.usuarioRepository = usuarioRepository;
		this.recursoRepository = recursoRepository;
	}

	private void atualizarStatusRecurso(Recurso recurso) {
		// Verifica se existe alguma reserva com status CONFIRMADA ou PENDENTE
		boolean hasActiveReservation = recurso.getReservas().stream()
				.anyMatch(reserva -> reserva.getStatus() == StatusReserva.RESERVADO || reserva.getStatus() == StatusReserva.PENDENTE);

		// Se existir reserva ativa, o recurso está OCUPADO
		if (hasActiveReservation) {
			recurso.setStatus(StatusReserva.OCUPADO);
		} else {
			// Caso todas as reservas sejam CANCELADAS ou finalizadas
			recurso.setStatus(StatusReserva.DISPONIVEL);
		}

		// Salva a atualização do status no recurso
		recursoRepository.save(recurso);
	}

	// Método para criar uma nova reserva
	public ReservaDTO criarReserva(ReservaRequest request) {
		try {
			// Valida usuário e recurso
			Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
					.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + request.getUsuarioId()));

			Recurso recurso = recursoRepository.findById(request.getRecursoId())
					.orElseThrow(() -> new IllegalArgumentException("Recurso não encontrado com ID: " + request.getRecursoId()));

			// Verifica conflitos de horário, ignorando reservas canceladas
			boolean isConflict = reservaRepository.findByRecursoId(recurso.getId())
					.stream()
					.filter(reserva -> !reserva.getStatus().equals(StatusReserva.CANCELADA)) // Ignora reservas canceladas
					.anyMatch(reserva -> reserva.getDataInicio().isBefore(request.getDataFim()) &&
							reserva.getDataFim().isAfter(request.getDataInicio()));

			if (isConflict) {
				throw new IllegalArgumentException("O recurso já está reservado para o período solicitado.");
			}

			// Determina o status da reserva com base no tipo de usuário
			StatusReserva status = switch (usuario.getTipo()) {
			case COORDENADOR, PROFESSOR -> StatusReserva.RESERVADO;
			case ALUNO -> StatusReserva.PENDENTE;
			default -> throw new IllegalArgumentException("Tipo de usuário inválido.");
			};

			// Cria e salva a reserva
			Reserva reserva = new Reserva();
			reserva.setDataInicio(request.getDataInicio());
			reserva.setDataFim(request.getDataFim());
			reserva.setRecursoAdicional(request.getRecursoAdicional());
			reserva.setStatus(status);
			reserva.setUsuario(usuario);
			reserva.setRecurso(recurso);

			reserva = reservaRepository.save(reserva);

			// Atualiza o status do recurso após a criação da reserva
			atualizarStatusRecurso(recurso);  // Chama o método para atualizar o status do recurso

			return new ReservaDTO(reserva);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao criar reserva: " + e.getMessage(), e);
		}
	}

	public void cancelarReserva(Long reservaId) {
		Reserva reserva = reservaRepository.findById(reservaId)
				.orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com ID: " + reservaId));

		reserva.setStatus(StatusReserva.CANCELADA);
		reservaRepository.save(reserva);

		// Atualiza o status do recurso associado
		Recurso recurso = reserva.getRecurso();
		atualizarStatusRecurso(recurso);
	}

	// Método para listar todas as reservas
	public List<ReservaDTO> listarTodas() {
		List<Reserva> reservas = reservaRepository.findAll();
		return reservas.stream()
				.map(ReservaDTO::new)
				.collect(Collectors.toList());
	}
}
