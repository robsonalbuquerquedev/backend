package br.edu.ifpe.manager.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import br.edu.ifpe.manager.model.Reserva;

@Getter
@Setter
@Data
public class ReservaRequest {

    @NotNull(message = "O recurso é obrigatório")
    private Long recursoId;

    @NotNull(message = "O usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "A data e hora de início são obrigatórias")
    @FutureOrPresent(message = "A data de início deve ser no presente ou no futuro")
    private LocalDateTime startDate;

    @NotNull(message = "A data e hora de término são obrigatórias")
    @FutureOrPresent(message = "A data de término deve ser no presente ou no futuro")
    private LocalDateTime endDate;

    private boolean includeAdditionalResource;

    private String additionalResource;
}
