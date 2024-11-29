package br.edu.ifpe.manager.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservaDTO {

    private Long id;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    private Long usuarioId; // ID do usuário associado à reserva

    private Long recursoId; // ID do recurso associado

    private Long recursoAdicionalId; // ID do recurso adicional associado (se aplicável)
}
