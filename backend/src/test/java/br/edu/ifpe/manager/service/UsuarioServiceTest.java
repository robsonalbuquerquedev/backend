package br.edu.ifpe.manager.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import br.edu.ifpe.manager.dto.UsuarioDTO;
import br.edu.ifpe.manager.infra.security.TokenService;
import br.edu.ifpe.manager.model.TipoUsuario;
import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.List;

public class UsuarioServiceTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private TokenService tokenService;

	@Mock
	private EmailService emailService;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UsuarioService usuarioService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this); // Inicializa os mocks
	}

	@Test
	public void testExisteEmail_QuandoEmailExiste() {
		// Arrange (Configuração)
		String email = "teste@example.com";
		when(usuarioRepository.findByEmail(email)).thenReturn(new Usuario());

		// Act (Execução)
		boolean resultado = usuarioService.existeEmail(email);

		// Assert (Verificação)
		assertTrue(resultado);
		verify(usuarioRepository, times(1)).findByEmail(email);
	}

	@Test
	public void testExisteEmail_QuandoEmailNaoExiste() {
		// Arrange (Configuração)
		String email = "inexistente@example.com";
		when(usuarioRepository.findByEmail(email)).thenReturn(null);

		// Act (Execução)
		boolean resultado = usuarioService.existeEmail(email);

		// Assert (Verificação)
		assertFalse(resultado);
		verify(usuarioRepository, times(1)).findByEmail(email);
	}

	@Test
	public void testListarTodos() {
		// Arrange (Configuração)
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("João");
		usuario.setEmail("joao@example.com");
		usuario.setTipo(TipoUsuario.ALUNO); // Usando o enum TipoUsuario
		usuario.setApproved(false);

		when(usuarioRepository.findAll()).thenReturn(Collections.singletonList(usuario));

		// Act (Execução)
		List<UsuarioDTO> resultado = usuarioService.listarTodos();

		// Assert (Verificação)
		assertEquals(1, resultado.size());
		assertEquals("João", resultado.get(0).getNome());
		assertEquals("joao@example.com", resultado.get(0).getEmail());
		verify(usuarioRepository, times(1)).findAll();
	}
}
