package br.edu.ifpe.manager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.ifpe.manager.dto.RecursoDTO;
import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.StatusReserva;
import br.edu.ifpe.manager.repository.RecursoRepository;
import br.edu.ifpe.manager.request.RecursoRequest;

@ExtendWith(MockitoExtension.class)
public class RecursoServiceTest {

	@Mock
	private RecursoRepository recursoRepository;

	@InjectMocks
	private RecursoService recursoService;

	private Recurso recurso;
	private RecursoRequest recursoRequest;

	@BeforeEach
	void setUp() {
		// Configuração de um recurso de exemplo
		recurso = new Recurso();
		recurso.setId(1L);
		recurso.setNome("Projetor");
		recurso.setDescricao("Projetor Full HD");
		recurso.setCapacidade(10);
		recurso.setLocalizacao("Sala 101");
		recurso.setStatus(StatusReserva.DISPONIVEL);

		// Configuração de um RecursoRequest de exemplo
		recursoRequest = new RecursoRequest();
		recursoRequest.setNome("Projetor");
		recursoRequest.setDescricao("Projetor Full HD");
		recursoRequest.setCapacidade(10);
		recursoRequest.setLocalizacao("Sala 101");
	}

	@Test
	void testListarRecursos() {
		// Configuração do mock
		when(recursoRepository.findAll()).thenReturn(Arrays.asList(recurso));

		// Execução do método
		List<RecursoDTO> recursos = recursoService.listarRecursos();

		// Verificações
		assertNotNull(recursos);
		assertEquals(1, recursos.size());
		assertEquals("Projetor", recursos.get(0).getNome());
		verify(recursoRepository, times(1)).findAll();
	}
	
	@Test
    void testListarRecursos_ListaVazia() {
        // Configuração do mock
        when(recursoRepository.findAll()).thenReturn(Collections.emptyList());

        // Execução do método
        List<RecursoDTO> recursos = recursoService.listarRecursos();

        // Verificações
        assertNotNull(recursos);
        assertTrue(recursos.isEmpty());
        verify(recursoRepository, times(1)).findAll();
    }
	
	@Test
    void testBuscarRecursosPorNome() {
        // Configuração do mock
        when(recursoRepository.findByNomeContainingIgnoreCase("Projetor")).thenReturn(Arrays.asList(recurso));

        // Execução do método
        List<RecursoDTO> recursos = recursoService.buscarRecursosPorNome("Projetor");

        // Verificações
        assertNotNull(recursos);
        assertEquals(1, recursos.size());
        assertEquals("Projetor", recursos.get(0).getNome());
        verify(recursoRepository, times(1)).findByNomeContainingIgnoreCase("Projetor");
    }
	
	@Test
    void testBuscarRecursosPorNome_NomeNaoEncontrado() {
        // Configuração do mock
        when(recursoRepository.findByNomeContainingIgnoreCase("Inexistente")).thenReturn(Collections.emptyList());

        // Execução do método
        List<RecursoDTO> recursos = recursoService.buscarRecursosPorNome("Inexistente");

        // Verificações
        assertNotNull(recursos);
        assertTrue(recursos.isEmpty());
        verify(recursoRepository, times(1)).findByNomeContainingIgnoreCase("Inexistente");
    }
	
	@Test
    void testBuscarRecursosPorLocalizacao() {
        // Configuração do mock
        when(recursoRepository.findByLocalizacao("Sala 101")).thenReturn(Arrays.asList(recurso));

        // Execução do método
        List<RecursoDTO> recursos = recursoService.buscarRecursosPorLocalizacao("Sala 101");

        // Verificações
        assertNotNull(recursos);
        assertEquals(1, recursos.size());
        assertEquals("Sala 101", recursos.get(0).getLocalizacao());
        verify(recursoRepository, times(1)).findByLocalizacao("Sala 101");
    }
	
	@Test
    void testBuscarRecursosPorLocalizacaoParcial() {
        // Configuração do mock
        when(recursoRepository.findByLocalizacaoContainingIgnoreCase("Sala")).thenReturn(Arrays.asList(recurso));

        // Execução do método
        List<RecursoDTO> recursos = recursoService.buscarRecursosPorLocalizacaoParcial("Sala");

        // Verificações
        assertNotNull(recursos);
        assertEquals(1, recursos.size());
        assertEquals("Sala 101", recursos.get(0).getLocalizacao());
        verify(recursoRepository, times(1)).findByLocalizacaoContainingIgnoreCase("Sala");
    }
	
	@Test
    void testSalvarRecurso_NovoRecurso() {
        // Configuração do mock
        when(recursoRepository.save(any(Recurso.class))).thenReturn(recurso);

        // Execução do método
        RecursoDTO recursoSalvo = recursoService.salvarRecurso(recursoRequest);

        // Verificações
        assertNotNull(recursoSalvo);
        assertEquals("Projetor", recursoSalvo.getNome());
        verify(recursoRepository, times(1)).save(any(Recurso.class));
    }
	
	@Test
    void testSalvarRecurso_AtualizarRecurso() {
        // Configuração do mock
        recursoRequest.setId(1L);
        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recurso));
        when(recursoRepository.save(any(Recurso.class))).thenReturn(recurso);

        // Execução do método
        RecursoDTO recursoSalvo = recursoService.salvarRecurso(recursoRequest);

        // Verificações
        assertNotNull(recursoSalvo);
        assertEquals("Projetor", recursoSalvo.getNome());
        verify(recursoRepository, times(1)).findById(1L);
        verify(recursoRepository, times(1)).save(any(Recurso.class));
    }
	
	@Test
    void testExcluirRecurso() {
        // Configuração do mock
        when(recursoRepository.existsById(1L)).thenReturn(true);

        // Execução do método
        recursoService.excluirRecurso(1L);

        // Verificações
        verify(recursoRepository, times(1)).existsById(1L);
        verify(recursoRepository, times(1)).deleteById(1L);
    }
	
	@Test
    void testExcluirRecurso_NaoEncontrado() {
        // Configuração do mock
        when(recursoRepository.existsById(1L)).thenReturn(false);

        // Execução e verificação de exceção
        assertThrows(IllegalArgumentException.class, () -> recursoService.excluirRecurso(1L));
        verify(recursoRepository, times(1)).existsById(1L);
        verify(recursoRepository, never()).deleteById(1L);
    }
	
	@Test
    void testToRecursoDTO_SemReservas() {
        // Configuração do recurso sem reservas
        recurso.setReservas(Collections.emptyList());

        // Execução do método
        RecursoDTO recursoDTO = recursoService.toRecursoDTO(recurso);

        // Verificações
        assertNotNull(recursoDTO);
        assertEquals(StatusReserva.DISPONIVEL, recursoDTO.getStatus());
        assertTrue(recursoDTO.getReservasIds().isEmpty());
    }
	
	@Test
    void testToRecursoDTO_ComReservaPendente() {
        // Configuração de uma reserva pendente
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        reserva.setStatus(StatusReserva.PENDENTE);
        recurso.setReservas(Arrays.asList(reserva));

        // Execução do método
        RecursoDTO recursoDTO = recursoService.toRecursoDTO(recurso);

        // Verificações
        assertNotNull(recursoDTO);
        assertEquals(StatusReserva.PENDENTE, recursoDTO.getStatus());
        assertEquals(1, recursoDTO.getReservasIds().size());
        assertEquals(1L, recursoDTO.getReservasIds().get(0));
    }

	@Test
	void testToRecursoDTO_ComReservaReservado() {
	    // Configuração de uma reserva reservada
	    Reserva reserva = new Reserva();
	    reserva.setId(1L);
	    reserva.setStatus(StatusReserva.RESERVADO);
	    recurso.setReservas(Arrays.asList(reserva));

	    // Execução do método
	    RecursoDTO recursoDTO = recursoService.toRecursoDTO(recurso);

	    // Verificações
	    assertNotNull(recursoDTO);
	    assertEquals(StatusReserva.RESERVADO, recursoDTO.getStatus());
	    assertEquals(1, recursoDTO.getReservasIds().size());
	    assertEquals(1L, recursoDTO.getReservasIds().get(0));
	}
}