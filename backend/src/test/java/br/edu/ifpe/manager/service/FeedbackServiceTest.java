package br.edu.ifpe.manager.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.edu.ifpe.manager.dto.FeedbackDTO;
import br.edu.ifpe.manager.model.Feedback;
import br.edu.ifpe.manager.repository.FeedbackRepository;
import br.edu.ifpe.manager.request.FeedbackRequest;

class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private FeedbackService feedbackService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks
    }

    @Test
    void testSalvarFeedback_SalvaNoRepositorio() {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest("João Silva", "joao.silva@example.com", "Ótimo serviço!");
        Feedback feedbackSalvo = new Feedback(1L, "João Silva", "joao.silva@example.com", "Ótimo serviço!");

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedbackSalvo);

        // Act
        feedbackService.salvarFeedback(feedbackRequest);

        // Assert
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void testSalvarFeedback_EnviaEmailAdmin() {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest("Maria Oliveira", "maria.oliveira@example.com", "Adorei o sistema!");
        Feedback feedbackSalvo = new Feedback(1L, "Maria Oliveira", "maria.oliveira@example.com", "Adorei o sistema!");

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedbackSalvo);

        // Act
        feedbackService.salvarFeedback(feedbackRequest);

        // Assert
        verify(emailService, times(1)).enviarEmailAdmin(feedbackSalvo);
    }

    @Test
    void testSalvarFeedback_EnviaEmailUsuario() {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest("Carlos Souza", "carlos.souza@example.com", "Sugestões de melhorias.");
        Feedback feedbackSalvo = new Feedback(1L, "Carlos Souza", "carlos.souza@example.com", "Sugestões de melhorias.");

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedbackSalvo);

        // Act
        feedbackService.salvarFeedback(feedbackRequest);

        // Assert
        verify(emailService, times(1)).enviarEmailUsuario(feedbackSalvo);
    }

    @Test
    void testSalvarFeedback_RetornaFeedbackDTO() {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest("Ana Costa", "ana.costa@example.com", "Sistema muito intuitivo.");
        Feedback feedbackSalvo = new Feedback(1L, "Ana Costa", "ana.costa@example.com", "Sistema muito intuitivo.");

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedbackSalvo);

        // Act
        FeedbackDTO resultado = feedbackService.salvarFeedback(feedbackRequest);

        // Assert
        assertNotNull(resultado);
        assertEquals(feedbackSalvo.getId(), resultado.getId());
        assertEquals(feedbackSalvo.getNome(), resultado.getNome());
        assertEquals(feedbackSalvo.getEmail(), resultado.getEmail());
        assertEquals(feedbackSalvo.getMensagem(), resultado.getMensagem());
    }

    @Test
    void testSalvarFeedback_ExcecaoAoSalvar() {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest("Pedro Alves", "pedro.alves@example.com", "Erro ao enviar feedback.");
        when(feedbackRepository.save(any(Feedback.class))).thenThrow(new RuntimeException("Erro no banco de dados"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> feedbackService.salvarFeedback(feedbackRequest));
    }
}