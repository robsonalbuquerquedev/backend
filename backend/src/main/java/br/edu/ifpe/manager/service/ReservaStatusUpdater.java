package br.edu.ifpe.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.StatusReserva;
import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.RecursoRepository;
import br.edu.ifpe.manager.repository.ReservaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaStatusUpdater {

	private final ReservaRepository reservaRepository;
	private final RecursoRepository recursoRepository;

	@Autowired
	private JavaMailSender mailSender; // Para envio de e-mails

	public ReservaStatusUpdater(ReservaRepository reservaRepository, RecursoRepository recursoRepository) {
		this.reservaRepository = reservaRepository;
		this.recursoRepository = recursoRepository;
	}

	@Scheduled(fixedRate = 60000) // Executa a cada 60 segundos
	@Transactional
	public void atualizarReservasExpiradas() {
		LocalDateTime agora = LocalDateTime.now();

		// Busca todas as reservas com status RESERVADO e cuja data_fim já passou
		List<Reserva> reservasExpiradas = reservaRepository.findByStatusAndDataFimBefore(StatusReserva.RESERVADO, agora);

		for (Reserva reserva : reservasExpiradas) {
			reserva.setStatus(StatusReserva.FINALIZADA); // Atualiza o status da reserva para FINALIZADA

			// Atualiza o status do recurso associado à reserva
			Recurso recurso = reserva.getRecurso();
			boolean hasActiveReservation = recurso.getReservas().stream()
					.anyMatch(r -> r.getStatus() == StatusReserva.RESERVADO || r.getStatus() == StatusReserva.PENDENTE);

			if (!hasActiveReservation) {
				recurso.setStatus(StatusReserva.DISPONIVEL); // Marca o recurso como DISPONÍVEL
			}

			recursoRepository.save(recurso); // Salva a atualização do recurso

			// Envia e-mail notificando o usuário sobre a finalização da reserva
			enviarEmailFinalizacao(reserva);
		}

		reservaRepository.saveAll(reservasExpiradas); // Salva as alterações nas reservas em lote
	}

	private void enviarEmailFinalizacao(Reserva reserva) {
		try {
			Usuario usuario = reserva.getUsuario();
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(usuario.getEmail());
			message.setSubject("Reserva Finalizada");

			// Corpo do e-mail
			message.setText(
					"Olá " + usuario.getNome() + ",\n\n" +
							"Informamos que sua reserva foi finalizada. Aqui estão os detalhes:\n\n" +
							"Recurso: " + reserva.getRecurso().getNome() + "\n" +
							"Data de Início: " + reserva.getDataInicio() + "\n" +
							"Data de Fim: " + reserva.getDataFim() + "\n" +
							"Status: " + reserva.getStatus() + "\n\n" +
							"Agradecemos por usar nosso sistema de reservas!\n\n" +
							"Atenciosamente,\n" +
							"Equipe de Reservas"
					);

			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Falha ao enviar o e-mail de finalização: " + e.getMessage());
		}
	}
}
