package br.edu.ifpe.manager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.UsuarioRepository;

public class AuthorizationServiceTest {
	@Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthorizationService authorizationService;

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
        when(usuarioRepository.findByEmail(email)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class,
            () -> authorizationService.loadUserByUsername(email)
        );
        
        assertEquals("User not found with email: " + email, exception.getMessage());
        verify(usuarioRepository).findByEmail(email);
    }
}
