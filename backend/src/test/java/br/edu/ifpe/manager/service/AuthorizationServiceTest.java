package br.edu.ifpe.manager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.UsuarioRepository;

public class AuthorizationServiceTest {
	@Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthorizationService authorizationService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Certifique-se de abrir os mocks
    }
    
    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        // Arrange
        String email = "user@example.com";
        Usuario mockUsuario = mock(Usuario.class);
        when(usuarioRepository.findByEmail(email)).thenReturn(mockUsuario);

        // Act
        UserDetails result = authorizationService.loadUserByUsername(email);

        // Assert
        assertNotNull(result);
        assertEquals(mockUsuario, result);
        verify(usuarioRepository).findByEmail(email);
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(null);  // Retorna null quando não encontrar o usuário

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class,
            () -> authorizationService.loadUserByUsername(email)
        );

        // Verificações
        assertEquals("User not found with email: " + email, exception.getMessage());
        verify(usuarioRepository).findByEmail(email);  // Verifica se o método foi chamado
    }
}

