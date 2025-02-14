package br.edu.ifpe.manager.integration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveRetornarListaDeUsuarios() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deveProcessarEsqueceuSenha() throws Exception {
        mockMvc.perform(post("/api/usuarios/esqueceu-senha")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"teste@teste.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Se o email estiver cadastrado, você receberá um link para redefinir sua senha."));
    }

    @Test
    void deveRedefinirSenha() throws Exception {
        mockMvc.perform(post("/api/usuarios/redefinir-senha")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\": \"valid-token\", \"novaSenha\": \"novaSenha123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Senha redefinida com sucesso."));
    }

    @Test
    void deveExibirMensagemToken() throws Exception {
        mockMvc.perform(get("/api/usuarios/redefinir-senha")
                .param("token", "valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Token válido. Você pode redefinir sua senha agora."));
    }
    
    @Test
    void deveRetornarErroParaTokenInvalido() throws Exception {
        mockMvc.perform(get("/api/usuarios/redefinir-senha")
                .param("token", "invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("Token inválido ou expirado."));
    }
}
