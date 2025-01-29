package br.edu.ifpe.manager.service;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import br.edu.ifpe.manager.model.Feedback;
import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.StatusReserva;
import br.edu.ifpe.manager.model.Usuario;

class EmailServiceTest {

	@Mock
	private JavaMailSender mailSender; // Mock do JavaMailSender

	@InjectMocks
	private EmailService emailService; // Injeta o mock no EmailService

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Inicializa os mocks
	}

	@Test
	void testEnviarEmailRedefinicao() {
		// Arrange
		Usuario usuario = new Usuario();
		usuario.setNome("João Silva");
		usuario.setEmail("joao.silva@example.com");
		String linkRedefinicao = "http://localhost:8080/redefinir-senha";

		// Act
		emailService.enviarEmailRedefinicao(usuario, linkRedefinicao);

		// Assert
		verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verifica se o email foi enviado
	}

	@Test
	void testEnviarEmailRedefinicao_Conteudo() {
		// Arrange
		Usuario usuario = new Usuario();
		usuario.setNome("João Silva");
		usuario.setEmail("joao.silva@example.com");
		String linkRedefinicao = "http://localhost:8080/redefinir-senha";

		// Act
		emailService.enviarEmailRedefinicao(usuario, linkRedefinicao);

		// Assert
		ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mailSender).send(captor.capture());

		SimpleMailMessage emailEnviado = captor.getValue();
		assertEquals("Redefinição de Senha", emailEnviado.getSubject()); // Verifica o assunto
		assertEquals("joao.silva@example.com", emailEnviado.getTo()[0]); // Verifica o destinatário
		assertTrue(emailEnviado.getText().contains(linkRedefinicao)); // Verifica se o link está no corpo
	}

	@Test
	void testEnviarEmailRedefinicao_Excecao() {
		// Arrange
		Usuario usuario = new Usuario();
		usuario.setNome("João Silva");
		usuario.setEmail("joao.silva@example.com");
		String linkRedefinicao = "http://localhost:8080/redefinir-senha";

		doThrow(new RuntimeException("Falha no envio do email")).when(mailSender).send(any(SimpleMailMessage.class));

		// Act
		emailService.enviarEmailRedefinicao(usuario, linkRedefinicao);

		// Assert
		verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verifica se o método foi chamado
	}

	@Test
	void testEnviarEmailAprovacao() {
		// Arrange
		Usuario usuario = new Usuario();
		usuario.setNome("Maria Oliveira");
		usuario.setEmail("maria.oliveira@example.com");
		String linkAprovacao = "http://localhost:8080/aprovar-cadastro";

		// Act
		emailService.enviarEmailAprovacao(usuario, linkAprovacao);

		// Assert
		verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Verifica se o email foi enviado
	}
	
	@Test
	void testEnviarEmailAprovacaoParaUsuario() {
	    // Arrange
	    Usuario usuario = new Usuario();
	    usuario.setNome("Carlos Souza");
	    usuario.setEmail("carlos.souza@example.com");

	    // Act
	    emailService.enviarEmailAprovacaoParaUsuario(usuario);

	    // Assert
	    verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}
	
	@Test
	void testEnviarEmailReserva() {
	    // Arrange
	    Usuario usuario = new Usuario();
	    usuario.setNome("Ana Costa");
	    usuario.setEmail("ana.costa@example.com");

	    Recurso recurso = new Recurso();
	    recurso.setNome("Sala de Reuniões 101");

	    Reserva reserva = new Reserva();
	    reserva.setUsuario(usuario);
	    reserva.setRecurso(recurso);
	    reserva.setDataInicio(LocalDateTime.of(2023, 10, 1, 10, 0));
	    reserva.setDataFim(LocalDateTime.of(2023, 10, 01, 11, 0));
	    reserva.setStatus(StatusReserva.RESERVADO);

	    // Act
	    emailService.enviarEmailReserva(usuario, reserva);

	    // Assert
	    verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}
	
	@Test
	void testEnviarEmailSolicitacaoReserva() {
	    // Arrange
	    Usuario usuario = new Usuario();
	    usuario.setNome("Pedro Alves");

	    Recurso recurso = new Recurso();
	    recurso.setNome("Auditório Principal");

	    Reserva reserva = new Reserva();
	    reserva.setUsuario(usuario);
	    reserva.setRecurso(recurso);
	    reserva.setDataInicio(LocalDateTime.of(2023, 10, 02, 14, 0));
	    reserva.setDataFim(LocalDateTime.of(2023, 10, 02, 16, 0));
	    reserva.setId(1L);

	    // Act
	    emailService.enviarEmailSolicitacaoReserva(reserva);

	    // Assert
	    verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}
	
	@Test
	void testEnviarEmailConfirmacaoReservaConfirmada() {
	    // Arrange
	    Usuario usuario = new Usuario();
	    usuario.setNome("Mariana Lima");
	    usuario.setEmail("mariana.lima@example.com");

	    Recurso recurso = new Recurso();
	    recurso.setNome("Laboratório de Informática");

	    Reserva reserva = new Reserva();
	    reserva.setUsuario(usuario);
	    reserva.setRecurso(recurso);
	    reserva.setDataInicio(LocalDateTime.of(2023, 10, 03, 9, 0));
	    reserva.setDataFim(LocalDateTime.of(2023, 10, 03, 11, 0));

	    // Act
	    emailService.enviarEmailConfirmacaoReservaConfirmada(reserva);

	    // Assert
	    verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}
	
	@Test
	void testEnviarEmailConfirmacaoReservaCancelada() {
	    // Arrange
	    Usuario usuario = new Usuario();
	    usuario.setNome("Ricardo Oliveira");
	    usuario.setEmail("ricardo.oliveira@example.com");

	    Recurso recurso = new Recurso();
	    recurso.setNome("Sala de Treinamento");

	    Reserva reserva = new Reserva();
	    reserva.setUsuario(usuario);
	    reserva.setRecurso(recurso);
	    reserva.setDataInicio(LocalDateTime.of(2023, 10, 04, 15, 0));
	    reserva.setDataFim(LocalDateTime.of(2023, 10, 04, 16, 0));

	    // Act
	    emailService.enviarEmailConfirmacaoReservaCancelada(reserva);

	    // Assert
	    verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}
	
	@Test
	void testEnviarEmailFinalizacao() {
	    // Arrange
	    Usuario usuario = new Usuario();
	    usuario.setNome("Fernanda Silva");
	    usuario.setEmail("fernanda.silva@example.com");

	    Recurso recurso = new Recurso();
	    recurso.setNome("Sala de Conferências");

	    Reserva reserva = new Reserva();
	    reserva.setUsuario(usuario);
	    reserva.setRecurso(recurso);
	    reserva.setDataInicio(LocalDateTime.of(2023, 10, 05, 8, 0));
	    reserva.setDataFim(LocalDateTime.of(2023, 10, 05, 10, 0));
	    reserva.setStatus(StatusReserva.FINALIZADA);

	    // Act
	    emailService.enviarEmailFinalizacao(reserva);

	    // Assert
	    verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}
	
	@Test
	void testEnviarEmailAdmin() {
	    // Arrange
	    Feedback feedback = new Feedback();
	    feedback.setNome("José Santos");
	    feedback.setEmail("jose.santos@example.com");
	    feedback.setMensagem("O sistema está ótimo, mas gostaria de mais funcionalidades.");

	    // Act
	    emailService.enviarEmailAdmin(feedback);

	    // Assert
	    verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}
	
	@Test
	void testEnviarEmailUsuario() {
	    // Arrange
	    Feedback feedback = new Feedback();
	    feedback.setNome("Camila Rocha");
	    feedback.setEmail("camila.rocha@example.com");
	    feedback.setMensagem("Adorei a usabilidade do sistema!");

	    // Act
	    emailService.enviarEmailUsuario(feedback);

	    // Assert
	    verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}
}