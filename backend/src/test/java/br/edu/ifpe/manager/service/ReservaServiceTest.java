package br.edu.ifpe.manager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.ifpe.manager.dto.ReservaDTO;
import br.edu.ifpe.manager.model.*;
import br.edu.ifpe.manager.repository.RecursoRepository;
import br.edu.ifpe.manager.repository.ReservaRepository;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import br.edu.ifpe.manager.request.ReservaRequest;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {
	
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

	    @Test
	    void criarReserva_UsuarioNaoEncontrado_DeveLancarExcecao() {
	        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
	        ReservaRequest request = new ReservaRequest();
	        request.setUsuarioId(1L);
	        assertThrows(IllegalArgumentException.class, () -> reservaService.criarReserva(request));
	    }

	    @Test
	    void criarReserva_RecursoNaoEncontrado_DeveLancarExcecao() {
	        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(mock(Usuario.class)));
	        when(recursoRepository.findById(1L)).thenReturn(Optional.empty());
	        ReservaRequest request = new ReservaRequest();
	        request.setRecursoId(1L);
	        assertThrows(IllegalArgumentException.class, () -> reservaService.criarReserva(request));
	    }

	    @Test
	    void criarReserva_ConflitoHorario_DeveLancarExcecao() {
	        Usuario usuario = mock(Usuario.class);
	        Recurso recurso = mock(Recurso.class);
	        Reserva reservaExistente = mock(Reserva.class);

	        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
	        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recurso));
	        when(reservaRepository.findByRecursoId(1L)).thenReturn(List.of(reservaExistente));
	        when(reservaExistente.getStatus()).thenReturn(StatusReserva.RESERVADO);
	        when(reservaExistente.getDataInicio()).thenReturn(LocalDateTime.now().minusHours(1));
	        when(reservaExistente.getDataFim()).thenReturn(LocalDateTime.now().plusHours(1));

	        ReservaRequest request = new ReservaRequest();
	        request.setDataInicio(LocalDateTime.now());
	        request.setDataFim(LocalDateTime.now().plusHours(2));
	        assertThrows(IllegalArgumentException.class, () -> reservaService.criarReserva(request));
	    }

	    @Test
	    void criarReserva_UsuarioCoordenador_StatusReservado() {
	        Usuario usuario = mock(Usuario.class);
	        Recurso recurso = mock(Recurso.class);
	        Reserva reservaSalva = mock(Reserva.class);

	        when(usuario.getTipo()).thenReturn(TipoUsuario.COORDENADOR);
	        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
	        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recurso));
	        when(reservaRepository.findByRecursoId(1L)).thenReturn(Collections.emptyList());
	        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaSalva);
	        when(reservaSalva.getStatus()).thenReturn(StatusReserva.RESERVADO);

	        ReservaDTO result = reservaService.criarReserva(new ReservaRequest());
	        
	        assertEquals(StatusReserva.RESERVADO, result.getStatus());
	        verify(emailService).enviarEmailReserva(usuario, reservaSalva);
	        verify(recursoRepository).save(recurso);
	    }

	    @Test
	    void criarReserva_UsuarioAluno_StatusPendente() {
	        Usuario usuario = mock(Usuario.class);
	        Recurso recurso = mock(Recurso.class);
	        Reserva reservaSalva = mock(Reserva.class);

	        when(usuario.getTipo()).thenReturn(TipoUsuario.ALUNO);
	        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
	        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recurso));
	        when(reservaRepository.findByRecursoId(1L)).thenReturn(Collections.emptyList());
	        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaSalva);
	        when(reservaSalva.getStatus()).thenReturn(StatusReserva.PENDENTE);

	        ReservaDTO result = reservaService.criarReserva(new ReservaRequest());
	        
	        assertEquals(StatusReserva.PENDENTE, result.getStatus());
	        verify(emailService).enviarEmailSolicitacaoReserva(reservaSalva);
	    }

	    @Test
	    void cancelarReserva_DeveAtualizarStatus() {
	        Reserva reserva = mock(Reserva.class);
	        Recurso recurso = mock(Recurso.class);
	        
	        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
	        when(reserva.getRecurso()).thenReturn(recurso);
	        when(recurso.getReservas()).thenReturn(Collections.singletonList(reserva));
	        when(reserva.getStatus()).thenReturn(StatusReserva.CANCELADA);

	        reservaService.cancelarReserva(1L);

	        verify(reserva).setStatus(StatusReserva.CANCELADA);
	        verify(recursoRepository).save(recurso);
	    }

	    @Test
	    void aprovarReserva_Pendente_DeveAtualizarParaReservado() {
	        Reserva reserva = mock(Reserva.class);
	        Recurso recurso = mock(Recurso.class);
	        
	        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
	        when(reserva.getStatus()).thenReturn(StatusReserva.PENDENTE);
	        when(reserva.getRecurso()).thenReturn(recurso);

	        reservaService.aprovarOuRejeitarReserva(1L, true);

	        verify(reserva).setStatus(StatusReserva.RESERVADO);
	        verify(emailService).enviarEmailConfirmacaoReservaConfirmada(reserva);
	    }

	    @Test
	    void listarTodas_DeveRetornarListaDTO() {
	        Reserva reserva = mock(Reserva.class);
	        when(reservaRepository.findAll()).thenReturn(Collections.singletonList(reserva));

	        List<ReservaDTO> result = reservaService.listarTodas();
	        
	        assertEquals(1, result.size());
	    }

}
