package br.edu.ifpe.manager.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.edu.ifpe.manager.model.Feedback;
import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.Usuario;

@Service
public class EmailService {

	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	private void enviarEmail(String destinatario, String assunto, String corpo) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(destinatario);
			message.setSubject(assunto);
			message.setText(corpo);
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Falha ao enviar o email para " + destinatario + ": " + e.getMessage());
		}
	}

	public void enviarEmailRedefinicao(Usuario usuario, String linkRedefinicao) {
		String assunto = "Redefinição de Senha";
		String corpo = "Olá " + usuario.getNome() + ",\n\n" +
				"Recebemos uma solicitação para redefinir sua senha. Para prosseguir, clique no link abaixo:\n\n" +
				linkRedefinicao + "\n\n" +
				"Se você não solicitou a redefinição, ignore este email.\n\n" +
				"Atenciosamente,\nEquipe do Sistema";
		enviarEmail(usuario.getEmail(), assunto, corpo);
	}

	public void enviarEmailAprovacao(Usuario usuario, String linkAprovacao) {
		String assunto = "Novo Cadastro de Usuário";
		String corpo = "Olá Admin,\n\n" +
				"Um novo usuário se cadastrou no sistema:\n" +
				"Nome: " + usuario.getNome() + "\n" +
				"Email: " + usuario.getEmail() + "\n\n" +
				"Para aprovar o cadastro, clique no link abaixo:\n" +
				linkAprovacao + "\n\n" +
				"Atenciosamente,\nEquipe do Sistema";
		enviarEmail("roomandlabmanagement@gmail.com", assunto, corpo);
	}

	public void enviarEmailAprovacaoParaUsuario(Usuario usuario) {
		String assunto = "Cadastro Aprovado";
		String corpo = "Olá " + usuario.getNome() + ",\n\n" +
				"Seu cadastro foi aprovado com sucesso! Agora você já pode acessar o sistema utilizando o link abaixo:\n\n" +
				"http://localhost:5173/login\n\n" +
				"Atenciosamente,\nEquipe do Sistema";
		enviarEmail(usuario.getEmail(), assunto, corpo);
	}

	public void enviarEmailReserva(Usuario usuario, Reserva reserva) {
		String assunto = "Confirmação da Reserva";
		String corpo = "Olá " + usuario.getNome() + ",\n\n" +
				"Sua reserva foi realizada com sucesso! Aqui estão os detalhes:\n\n" +
				"Recurso: " + reserva.getRecurso().getNome() + "\n" +
				"Data de Início: " + reserva.getDataInicio() + "\n" +
				"Data de Fim: " + reserva.getDataFim() + "\n" +
				"Status: " + reserva.getStatus() + "\n\n" +
				"Se precisar de mais informações, entre em contato conosco.\n\n" +
				"Atenciosamente,\nEquipe de Reservas";
		enviarEmail(usuario.getEmail(), assunto, corpo);
	}

	public void enviarEmailSolicitacaoReserva(Reserva reserva) {
		String assunto = "Solicitação de Reserva - Ação Requerida";
		String corpo = "Olá Admin,\n\n" +
				"O aluno " + reserva.getUsuario().getNome() + " solicitou uma reserva para o recurso " +
				reserva.getRecurso().getNome() + " no período de " +
				reserva.getDataInicio() + " até " + reserva.getDataFim() + ".\n\n" +
				"Para aprovar ou rejeitar a reserva, clique no link:\n" +
				"http://localhost:5173/approveReserva/" + reserva.getId() + "\n\n" +
				"Atenciosamente,\nEquipe do Sistema";
		enviarEmail("roomandlabmanagement@gmail.com", assunto, corpo);
	}

	public void enviarEmailConfirmacaoReservaConfirmada(Reserva reserva) {
		String assunto = "Reserva Confirmada";
		String corpo = "Olá " + reserva.getUsuario().getNome() + ",\n\n" +
				"Sua solicitação de reserva para o recurso " + reserva.getRecurso().getNome() + " foi confirmada!" +
				"\nPeríodo: " + reserva.getDataInicio() + " até " + reserva.getDataFim() + "\n\n" +
				"Agora você pode acessar o sistema para visualizar mais detalhes ou gerenciar suas reservas.\n\n" +
				"Atenciosamente,\nEquipe do Sistema";
		enviarEmail(reserva.getUsuario().getEmail(), assunto, corpo);
	}

	public void enviarEmailConfirmacaoReservaCancelada(Reserva reserva) {
		String assunto = "Reserva Cancelada";
		String corpo = "Olá " + reserva.getUsuario().getNome() + ",\n\n" +
				"Sua solicitação de reserva para o recurso " + reserva.getRecurso().getNome() + " foi cancelada!" +
				"\nPeríodo: " + reserva.getDataInicio() + " até " + reserva.getDataFim() + "\n\n" +
				"Atenciosamente,\nEquipe do Sistema";
		enviarEmail(reserva.getUsuario().getEmail(), assunto, corpo);
	}

	public void enviarEmailFinalizacao(Reserva reserva) {
		String assunto = "Reserva Finalizada";
		String corpo = "Olá " + reserva.getUsuario().getNome() + ",\n\n" +
				"Informamos que sua reserva foi finalizada. Aqui estão os detalhes:\n\n" +
				"Recurso: " + reserva.getRecurso().getNome() + "\n" +
				"Data de Início: " + reserva.getDataInicio() + "\n" +
				"Data de Fim: " + reserva.getDataFim() + "\n" +
				"Status: " + reserva.getStatus() + "\n\n" +
				"Agradecemos por usar nosso sistema de reservas!\n\n" +
				"Atenciosamente,\nEquipe de Reservas";
		enviarEmail(reserva.getUsuario().getEmail(), assunto, corpo);
	}

	public void enviarEmailAdmin(Feedback feedback) {
		String assunto = "Novo Feedback Recebido";
		String corpo = "Nome: " + feedback.getNome() + "\n" +
				"E-mail: " + feedback.getEmail() + "\n" +
				"Mensagem: " + feedback.getMensagem();
		enviarEmail("admin@dominio.com", assunto, corpo);
	}

	public void enviarEmailUsuario(Feedback feedback) {
		String assunto = "Agradecemos pelo seu Feedback!";
		String corpo = "Olá " + feedback.getNome() + ",\n\n" +
				"Agradecemos por enviar o seu feedback! Sua opinião é muito importante para nós.\n\n" +
				"Em breve, nossa equipe irá revisar suas sugestões e comentários. Se necessário, entraremos em contato.\n\n" +
				"Atenciosamente,\n" +
				"Equipe do Sistema";
		enviarEmail(feedback.getEmail(), assunto, corpo);
	}
}
