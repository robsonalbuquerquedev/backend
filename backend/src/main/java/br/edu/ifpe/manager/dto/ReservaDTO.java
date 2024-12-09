package br.edu.ifpe.manager.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservaDTO {

    private Long id;

    @NotNull(message = "A data de início não pode ser nula")
    @FutureOrPresent(message = "A data de início deve ser no futuro ou presente")
    private LocalDateTime dataInicio;

    @NotNull(message = "A data de fim não pode ser nula")
    @FutureOrPresent(message = "A data de fim deve ser no futuro ou presente")
    private LocalDateTime dataFim;

    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId; // ID do usuário associado à reserva

    @NotNull(message = "O ID do recurso é obrigatório")
    private Long recursoId; // ID do recurso associado

    private String recursoAdicional; // Descrição do recurso adicional (se aplicável)
}
