package br.edu.ifpe.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.edu.ifpe.manager.dto.FeedbackDTO;
import br.edu.ifpe.manager.model.Feedback;
import br.edu.ifpe.manager.repository.FeedbackRepository;
import br.edu.ifpe.manager.request.FeedbackRequest;

@Service
public class FeedbackService {

	@Autowired
	private FeedbackRepository feedbackRepository;

	@Autowired
	private JavaMailSender mailSender;

	public FeedbackDTO salvarFeedback(FeedbackRequest feedbackRequest) {
		// Converte FeedbackRequest para Feedback (entidade)
		Feedback feedback = new Feedback();
		feedback.setNome(feedbackRequest.getNome());
		feedback.setEmail(feedbackRequest.getEmail());
		feedback.setMensagem(feedbackRequest.getMensagem());

		// Salva feedback no banco de dados
		Feedback savedFeedback = feedbackRepository.save(feedback);

		// Envia e-mail
		enviarEmail(savedFeedback);

		// Retorna o FeedbackDTO com os dados do feedback salvo
		return new FeedbackDTO(savedFeedback.getId(), savedFeedback.getNome(), savedFeedback.getEmail(), savedFeedback.getMensagem());
	}

	private void enviarEmail(Feedback feedback) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("admin@dominio.com");
		message.setSubject("Novo Feedback Recebido");
		message.setText("Nome: " + feedback.getNome() + "\n" +
				"E-mail: " + feedback.getEmail() + "\n" +
				"Mensagem: " + feedback.getMensagem());

		mailSender.send(message);
	}
}
