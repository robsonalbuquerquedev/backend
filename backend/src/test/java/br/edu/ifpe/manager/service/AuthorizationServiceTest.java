package br.edu.ifpe.manager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

class AuthenticationServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthorizationService authorizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks
    }

    @Test
    void testLoadUserByUsername_UsuarioExistente() {
        // Arrange: Criando um usuário simulado
        Usuario usuarioMock = new Usuario();
        usuarioMock.setEmail("usuario@example.com");
        usuarioMock.setSenha("senha123"); // Supondo que tem um atributo senha

        when(usuarioRepository.findByEmail("usuario@example.com"))
                .thenReturn(Optional.of(usuarioMock));

        // Act: Chama o método
        UserDetails userDetails = authorizationService.loadUserByUsername("usuario@example.com");

        // Assert: Verifica se os detalhes estão corretos
        assertNotNull(userDetails);
        assertEquals("usuario@example.com", userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UsuarioNaoEncontrado() {
        // Arrange: Simula que o usuário não existe
        when(usuarioRepository.findByEmail("usuario_inexistente@example.com"))
                .thenReturn(Optional.empty());

        // Act & Assert: Espera que o método lance uma exceção
        assertThrows(UsernameNotFoundException.class, 
            () -> authorizationService.loadUserByUsername("usuario_inexistente@example.com"));
    }
}

