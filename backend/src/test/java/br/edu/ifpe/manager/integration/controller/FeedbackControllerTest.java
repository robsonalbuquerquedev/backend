package br.edu.ifpe.manager.integration.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.edu.ifpe.manager.request.FeedbackRequest;
import br.edu.ifpe.manager.service.FeedbackService;

@SpringBootTest
@AutoConfigureMockMvc
public class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private FeedbackService feedbackService;


    @Test
    void deveRetornar201AoEnviarFeedbackComSucesso() throws Exception {
        FeedbackRequest request = new FeedbackRequest();
        request.setNome("Robson");
        request.setEmail("robson@email.com");
        request.setMensagem("Ótimo serviço!");

        mockMvc.perform(post("/api/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
    
    @Test
    void deveRetornar400SeRequisicaoForInvalida() throws Exception {
        FeedbackRequest request = new FeedbackRequest(); // Requisição sem mensagem

        mockMvc.perform(post("/api/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar500SeServicoLancarExcecao() throws Exception {
        FeedbackRequest request = new FeedbackRequest();
        request.setNome("Robson");
        request.setEmail("robson@email.com");
        request.setMensagem("Ótimo serviço!");

        // Simulando falha ao salvar feedback
        when(feedbackService.salvarFeedback(any(FeedbackRequest.class)))
                .thenThrow(new RuntimeException("Erro interno do servidor"));

        mockMvc.perform(post("/api/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

}
