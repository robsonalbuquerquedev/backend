package br.edu.ifpe.manager.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservaRequest {

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Long usuarioId;

    @NotNull(message = "O ID do recurso é obrigatório.")
    private Long recursoId;

    @NotNull(message = "A data e hora de início são obrigatórias.")
    private LocalDateTime dataHoraInicio;

    @NotNull(message = "A data e hora final são obrigatórias.")
    private LocalDateTime dataHoraFim;

    @Size(max = 255, message = "A descrição do recurso adicional não pode exceder 255 caracteres.")
    private String recursoAdicional;
}
