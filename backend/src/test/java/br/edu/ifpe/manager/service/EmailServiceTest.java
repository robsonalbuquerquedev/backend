package br.edu.ifpe.manager.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

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
}