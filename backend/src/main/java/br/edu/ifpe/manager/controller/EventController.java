package br.edu.ifpe.manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpe.manager.dto.EventDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @GetMapping
    public ResponseEntity<Map<String, Map<String, List<EventDTO>>>> getEvents() {
        // Estrutura principal: Ano -> Categorias (feriados/eventos) -> Lista de eventos
        Map<String, Map<String, List<EventDTO>>> eventsByYear = new HashMap<>();

        // Dados de 2025
        Map<String, List<EventDTO>> events2025 = new HashMap<>();
        events2025.put("feriados", getHolidays2025());
        events2025.put("eventos", getEvents2025());
        eventsByYear.put("2025", events2025);

        // Dados de 2026 (exemplo de como adicionar novos anos)
        Map<String, List<EventDTO>> events2026 = new HashMap<>();
        events2026.put("feriados", getHolidays2026());
        events2026.put("eventos", getEvents2026());
        eventsByYear.put("2026", events2026);

        // Retorna o mapa completo
        return ResponseEntity.ok(eventsByYear);
    }

    private List<EventDTO> getHolidays2025() {
        List<EventDTO> holidays = new ArrayList<>();
        holidays.add(new EventDTO(1, "01/01", "quarta-feira - Confraternização Universal"));
        holidays.add(new EventDTO(2, "03/03", "segunda-feira - Carnaval"));
        holidays.add(new EventDTO(3, "04/03", "terça-feira - Carnaval"));
        holidays.add(new EventDTO(4, "18/04", "sexta-feira - Paixão de Cristo"));
        holidays.add(new EventDTO(5, "21/04", "segunda-feira - Tiradentes"));
        holidays.add(new EventDTO(6, "01/05", "quinta-feira - Dia do Trabalho"));
        holidays.add(new EventDTO(7, "19/06", "quinta-feira - Corpus Christi"));
        holidays.add(new EventDTO(8, "07/09", "domingo - Independência do Brasil"));
        holidays.add(new EventDTO(9, "12/10", "domingo - Nossa Sr.a Aparecida - Padroeira do Brasil"));
        holidays.add(new EventDTO(10, "02/11", "domingo - Finados"));
        holidays.add(new EventDTO(11, "15/11", "sábado - Proclamação da República"));
        holidays.add(new EventDTO(12, "20/11", "quinta-feira - Dia Nacional de Zumbi e da Consciência Negra"));
        holidays.add(new EventDTO(13, "25/12", "quinta-feira - Natal"));
        return holidays;
    }

    private List<EventDTO> getEvents2025() {
        List<EventDTO> events = new ArrayList<>();
        events.add(new EventDTO(14, "09/03", "Atividade - Dia Internacional das Mulheres"));
        events.add(new EventDTO(15, "26/04", "Dia do Livro"));
        events.add(new EventDTO(16, "30/04", "Festival Latino-americano de Instalação de Software Livre (FLISOL)"));
        events.add(new EventDTO(17, "24/06", "São João"));
        return events;
    }

    private List<EventDTO> getHolidays2026() {
        List<EventDTO> holidays = new ArrayList<>();
        holidays.add(new EventDTO(18, "01/01", "Confraternização Universal"));
        holidays.add(new EventDTO(19, "21/04", "Tiradentes"));
        return holidays;
    }

    private List<EventDTO> getEvents2026() {
        List<EventDTO> events = new ArrayList<>();
        events.add(new EventDTO(20, "14/02", "Carnaval"));
        return events;
    }
}
