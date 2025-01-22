package br.edu.ifpe.manager.service;

import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.StatusReserva;
import br.edu.ifpe.manager.model.TipoUsuario;
import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.dto.ReservaDTO;
import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.repository.ReservaRepository;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import br.edu.ifpe.manager.request.ReservaRequest;
import br.edu.ifpe.manager.repository.RecursoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

	@Autowired
	private JavaMailSender mailSender;

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

			// Envia notificação por e-mail para o usuário
			enviarEmailReserva(usuario, reserva);

			// Envia notificação por e-mail para o ADMIN somente se o usuário for um ALUNO
			if (usuario.getTipo() == TipoUsuario.ALUNO) {
				enviarEmailSolicitacaoReserva(reserva);
			}

			return new ReservaDTO(reserva);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao criar reserva: " + e.getMessage(), e);
		}
	}

	private void enviarEmailReserva(Usuario usuario, Reserva reserva) {
		try {
			// Configura os detalhes do e-mail
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(usuario.getEmail());
			message.setSubject("Confirmação da Reserva");

			// Corpo do e-mail com detalhes da reserva
			message.setText(
					"Olá " + usuario.getNome() + ",\n\n" +
							"Sua reserva foi realizada com sucesso! Aqui estão os detalhes:\n\n" +
							"Recurso: " + reserva.getRecurso().getNome() + "\n" +
							"Data de Início: " + reserva.getDataInicio() + "\n" +
							"Data de Fim: " + reserva.getDataFim() + "\n" +
							"Status: " + reserva.getStatus() + "\n\n" +
							"Se precisar de mais informações, entre em contato conosco.\n\n" +
							"Atenciosamente,\n" +
							"Equipe de Reservas"
					);

			// Envia o e-mail
			mailSender.send(message);
		} catch (Exception e) {
			// Log ou tratamento de erros no envio do e-mail
			e.printStackTrace();
			System.err.println("Falha ao enviar o e-mail de notificação: " + e.getMessage());
		}
	}

	private void enviarEmailSolicitacaoReserva(Reserva reserva) {
		try {
			String urlAprovacao = "http://localhost:5173/approveReserva/" + reserva.getId();
			String assunto = "Solicitação de Reserva - Ação Requerida";
			String corpo = "Olá Admin,\n\n" +
					"O aluno " + reserva.getUsuario().getNome() + " solicitou uma reserva para o recurso " +
					reserva.getRecurso().getNome() + " no período de " +
					reserva.getDataInicio() + " até " + reserva.getDataFim() + ".\n\n" +
					"Para aprovar ou rejeitar a reserva, clique no link:\n" +
					urlAprovacao + "\n\n" +
					"Atenciosamente,\n" +
					"Equipe do Sistema";

			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo("roomandlabmanagement@gmail.com");
			message.setSubject(assunto);
			message.setText(corpo);

			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Falha ao enviar o e-mail para o ADMIN: " + e.getMessage());
		}
	}

	public void aprovarReserva(Long reservaId) {
		// Recupera a reserva e o recurso
		Reserva reserva = reservaRepository.findById(reservaId)
				.orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com ID: " + reservaId));
		Recurso recurso = reserva.getRecurso();

		// Verifica se a reserva ainda está pendente
		if (reserva.getStatus() != StatusReserva.PENDENTE) {
			throw new IllegalArgumentException("A reserva não está pendente.");
		}

		// Aprova a reserva e altera o status
		reserva.setStatus(StatusReserva.RESERVADO);
		reservaRepository.save(reserva);

		// Atualiza o status do recurso
		atualizarStatusRecurso(recurso);

		// Envia o e-mail de confirmação de reserva
		enviarEmailConfirmacaoReservaConfirmada(reserva);
	}


	private void enviarEmailConfirmacaoReservaConfirmada(Reserva reserva) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(reserva.getUsuario().getEmail());
			message.setSubject("Reserva Confirmada");

			String corpo = "Olá " + reserva.getUsuario().getNome() + ",\n\n" +
					"Sua solicitação de reserva para o recurso " + reserva.getRecurso().getNome() + " foi confirmada!" +
					"\nPeríodo: " + reserva.getDataInicio() + " até " + reserva.getDataFim() + "\n\n" +
					"Agora você pode acessar o sistema para visualizar mais detalhes ou gerenciar suas reservas.\n\n" +
					"Atenciosamente,\n" +
					"Equipe do Sistema";
			message.setText(corpo);
			mailSender.send(message);  
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Falha ao enviar o e-mail de confirmação para o ALUNO: " + e.getMessage());
		}
	}

	public void rejeitarReserva(Long reservaId) {
		// Recupera a reserva
		Reserva reserva = reservaRepository.findById(reservaId)
				.orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com ID: " + reservaId));

		// Verifica se a reserva ainda está pendente
		if (reserva.getStatus() != StatusReserva.PENDENTE) {
			throw new IllegalArgumentException("A reserva não está pendente.");
		}

		// Rejeita a reserva e altera o status
		reserva.setStatus(StatusReserva.CANCELADA);
		reservaRepository.save(reserva);

		// Envia o e-mail de confirmação de cancelamento de reserva
		enviarEmailConfirmacaoReservaCancelada(reserva);
	}

	private void enviarEmailConfirmacaoReservaCancelada(Reserva reserva) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(reserva.getUsuario().getEmail());
			message.setSubject("Reserva Confirmada");

			String corpo = "Olá " + reserva.getUsuario().getNome() + ",\n\n" +
					"Sua solicitação de reserva para o recurso " + reserva.getRecurso().getNome() + " foi cancelada!" +
					"\nPeríodo: " + reserva.getDataInicio() + " até " + reserva.getDataFim() + "\n\n" +
					"Atenciosamente,\n" +
					"Equipe do Sistema";
			message.setText(corpo);
			mailSender.send(message);  
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Falha ao enviar o e-mail de confirmação para o ALUNO: " + e.getMessage());
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

	/*public void aprovarOuRejeitarReserva(Long id, Boolean aprovado) {
		Reserva reserva = reservaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com ID: " + id));

		// Se o status da reserva for PENDENTE, podemos aprovar ou rejeitar
		if (reserva.getStatus() == StatusReserva.PENDENTE) {
			if (aprovado) {
				reserva.setStatus(StatusReserva.RESERVADO); // Aprova e torna a reserva RESERVADA
			} else {
				reserva.setStatus(StatusReserva.CANCELADA); // Rejeita e torna a reserva CANCELADA
			}

			// Atualiza a reserva no banco de dados
			reservaRepository.save(reserva);

			// Após atualizar a reserva, podemos também atualizar o status do recurso
			atualizarStatusRecurso(reserva.getRecurso());
		} else {
			throw new IllegalArgumentException("A reserva não está em status PENDENTE, não pode ser aprovada ou rejeitada.");
		}
	}*/

	// Método para listar todas as reservas
	public List<ReservaDTO> listarTodas() {
		List<Reserva> reservas = reservaRepository.findAll();
		return reservas.stream()
				.map(ReservaDTO::new)
				.collect(Collectors.toList());
	}

	public List<Reserva> buscarReservasPorStatus(String status) {
		if (status == null || status.isEmpty()) {
			throw new IllegalArgumentException("O status não pode ser nulo ou vazio.");
		}
		return reservaRepository.findByStatus(status);
	}
}
