package br.edu.ifpe.manager.integration.controller;

import br.edu.ifpe.manager.controller.EventController;
import br.edu.ifpe.manager.dto.EventDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EventControllerIntegrationTest {

    @Autowired
    private EventController eventController;

    @Test
    public void testGetEvents() {
        // Act
        ResponseEntity<Map<String, Map<String, List<EventDTO>>>> response = eventController.getEvents();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        Map<String, Map<String, List<EventDTO>>> eventsByYear = response.getBody();
        assertNotNull(eventsByYear);

        // Verifica se os anos estão presentes
        assertTrue(eventsByYear.containsKey("2025"));
        assertTrue(eventsByYear.containsKey("2026"));

        // Verifica os feriados de 2025
        List<EventDTO> holidays2025 = eventsByYear.get("2025").get("feriados");
        assertNotNull(holidays2025);
        assertEquals(13, holidays2025.size());

        // Verifica os eventos de 2025
        List<EventDTO> events2025 = eventsByYear.get("2025").get("eventos");
        assertNotNull(events2025);
        assertEquals(4, events2025.size());

        // Verifica os feriados de 2026
        List<EventDTO> holidays2026 = eventsByYear.get("2026").get("feriados");
        assertNotNull(holidays2026);
        assertEquals(2, holidays2026.size());

        // Verifica os eventos de 2026
        List<EventDTO> events2026 = eventsByYear.get("2026").get("eventos");
        assertNotNull(events2026);
        assertEquals(1, events2026.size());
    }
}