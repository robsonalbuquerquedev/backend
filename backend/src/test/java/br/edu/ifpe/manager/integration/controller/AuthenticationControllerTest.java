package br.edu.ifpe.manager.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import br.edu.ifpe.manager.dto.AuthenticationDTO;
import br.edu.ifpe.manager.dto.RegisterDTO;
import br.edu.ifpe.manager.model.TipoUsuario;
import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import br.edu.ifpe.manager.service.EmailService;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private EmailService emailService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(
            "Teste", 
            "teste@email.com", 
            new BCryptPasswordEncoder().encode("senha123"), 
            TipoUsuario.ALUNO, // Usando o enum TipoUsuario
            false // isApproved
        );
        usuario.setId(1L);
    }
    
    @Test
    void testRegisterUser() throws Exception {
    	RegisterDTO registerDTO = new RegisterDTO(
    		    "Novo Usuário", 
    		    "novo@email.com", 
    		    "senha123", 
    		    TipoUsuario.ALUNO, // Usando o enum TipoUsuario
    		    false // isApproved
    		);

        Mockito.when(usuarioRepository.findByEmail(registerDTO.email())).thenReturn(null);
        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuário registrado e aguardando aprovação."));
    }

    @Test
    void testLoginSuccess() throws Exception {
        AuthenticationDTO loginDTO = new AuthenticationDTO("teste@email.com", "senha123");

        Mockito.when(usuarioRepository.findByEmail(loginDTO.email())).thenReturn(usuario);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isForbidden()) // O usuário ainda não foi aprovado
                .andExpect(content().string("Seu cadastro ainda não foi aprovado."));
    }

    @Test
    void testLoginFail() throws Exception {
        AuthenticationDTO loginDTO = new AuthenticationDTO("emailinvalido@email.com", "senhaerrada");

        Mockito.when(usuarioRepository.findByEmail(loginDTO.email())).thenReturn(null);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Email ou senha inválidos."));
    }

    @Test
    void testApproveUserSuccess() throws Exception {
        usuario.setApproved(false);

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(get("/api/auth/approve/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário aprovado com sucesso."));
    }

    @Test
    void testApproveUserAlreadyApproved() throws Exception {
        usuario.setApproved(true);

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/auth/approve/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("O usuário já foi aprovado."));
    }

    @Test
    void testApproveUserNotFound() throws Exception {
        Mockito.when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/auth/approve/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado."));
    }
}