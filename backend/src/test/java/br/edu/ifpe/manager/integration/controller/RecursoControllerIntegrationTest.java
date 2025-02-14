package br.edu.ifpe.manager.integration.controller;

import br.edu.ifpe.manager.request.RecursoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class RecursoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarTodosOsRecursos() throws Exception {
        mockMvc.perform(get("/api/recursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveBuscarRecursosPorNome() throws Exception {
        mockMvc.perform(get("/api/recursos/nome/Computador"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveSalvarUmRecurso() throws Exception {
        RecursoRequest recursoRequest = new RecursoRequest();
        recursoRequest.setNome("Projetor");
        recursoRequest.setLocalizacao("Sala 101");

        mockMvc.perform(post("/api/recursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recursoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Projetor"))
                .andExpect(jsonPath("$.localizacao").value("Sala 101"));
    }

    @Test
    void deveAtualizarUmRecurso() throws Exception {
        RecursoRequest recursoRequest = new RecursoRequest();
        recursoRequest.setNome("Monitor");
        recursoRequest.setLocalizacao("Sala 102");

        mockMvc.perform(put("/api/recursos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recursoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Monitor"));
    }

    @Test
    void deveExcluirUmRecurso() throws Exception {
        mockMvc.perform(delete("/api/recursos/1"))
                .andExpect(status().isNoContent());
    }
}
