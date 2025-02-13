package br.edu.ifpe.manager.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.edu.ifpe.manager.dto.ReservaDTO;
import br.edu.ifpe.manager.model.*;
import br.edu.ifpe.manager.repository.ReservaRepository;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import br.edu.ifpe.manager.repository.RecursoRepository;
import br.edu.ifpe.manager.request.ReservaRequest;

class ReservaServiceTest {

	@Mock
	private ReservaRepository reservaRepository;

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private RecursoRepository recursoRepository;

	@Mock
	private EmailService emailService;

	@InjectMocks
	private ReservaService reservaService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCriarReserva_Sucesso() {
		// Arrange
		Usuario usuario = new Usuario(1L, "João", "joao@example.com", TipoUsuario.PROFESSOR);
		Recurso recurso = new Recurso(1L, "Sala 101", StatusReserva.DISPONIVEL);
		ReservaRequest request = new ReservaRequest(
				LocalDateTime.now(),               
				LocalDateTime.now().plusHours(2),   
				"Projetor",                         
				1L,                                 
				1L,                                 
				null                                
				);
		Reserva reserva = new Reserva(1L, request.getDataInicio(), request.getDataFim(), request.getRecursoAdicional(), usuario, recurso, StatusReserva.RESERVADO);

		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
		when(recursoRepository.findById(1L)).thenReturn(Optional.of(recurso));
		when(reservaRepository.findByRecursoId(1L)).thenReturn(List.of());
		when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

		// Act
		ReservaDTO resultado = reservaService.criarReserva(request);

		// Assert
		assertNotNull(resultado);
		assertEquals(StatusReserva.RESERVADO, resultado.getStatus());
		verify(emailService, times(1)).enviarEmailReserva(usuario, reserva);
	}

	@Test
	void testCriarReserva_ComConflito() {
		// Arrange
		Usuario usuario = new Usuario(1L, "Maria", "maria@example.com", TipoUsuario.ALUNO);
		Recurso recurso = new Recurso(1L, "Sala 102", StatusReserva.DISPONIVEL);
		LocalDateTime inicio = LocalDateTime.now();
		LocalDateTime fim = inicio.plusHours(2);
		Reserva reservaExistente = new Reserva(2L, inicio, fim, "Projetor", usuario, recurso, StatusReserva.RESERVADO);
		ReservaRequest request = new ReservaRequest(
				inicio.plusMinutes(30),
				fim.plusMinutes(30),      
				"Notebook",               
				1L,                       
				1L,                       
				null                      
				);

		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
		when(recursoRepository.findById(1L)).thenReturn(Optional.of(recurso));
		when(reservaRepository.findByRecursoId(1L)).thenReturn(List.of(reservaExistente));

		// Act & Assert
		Exception exception = assertThrows(RuntimeException.class, () -> reservaService.criarReserva(request));
		assertEquals("Erro ao criar reserva: O recurso já está reservado para o período solicitado.", exception.getMessage());
	}

	@Test
	void testCancelarReserva() {
		// Arrange
		Usuario usuario = new Usuario(1L, "Ana", "ana@example.com", TipoUsuario.PROFESSOR);
		Recurso recurso = new Recurso(1L, "Sala 103", StatusReserva.OCUPADO);
		Reserva reserva = new Reserva(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Microfone", usuario, recurso, StatusReserva.RESERVADO);

		when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

		// Act
		reservaService.cancelarReserva(1L);

		// Assert
		assertEquals(StatusReserva.CANCELADA, reserva.getStatus());
		verify(reservaRepository, times(1)).save(reserva);
	}

	@Test
	void testAprovarReserva() {
		// Arrange
		Usuario usuario = new Usuario(1L, "Carlos", "carlos@example.com", TipoUsuario.ALUNO);
		Recurso recurso = new Recurso(1L, "Sala 104", StatusReserva.DISPONIVEL);
		Reserva reserva = new Reserva(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Quadro", usuario, recurso, StatusReserva.PENDENTE);

		when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

		// Act
		reservaService.aprovarOuRejeitarReserva(1L, true);

		// Assert
		assertEquals(StatusReserva.RESERVADO, reserva.getStatus());
		verify(emailService, times(1)).enviarEmailConfirmacaoReservaConfirmada(reserva);
	}

	@Test
	void testRejeitarReserva() {
		// Arrange
		Usuario usuario = new Usuario(1L, "Lucas", "lucas@example.com", TipoUsuario.ALUNO);
		Recurso recurso = new Recurso(1L, "Sala 105", StatusReserva.DISPONIVEL);
		Reserva reserva = new Reserva(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Notebook", usuario, recurso, StatusReserva.PENDENTE);

		when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

		// Act
		reservaService.aprovarOuRejeitarReserva(1L, false);

		// Assert
		assertEquals(StatusReserva.CANCELADA, reserva.getStatus());
		verify(emailService, times(1)).enviarEmailConfirmacaoReservaCancelada(reserva);
	}
}
