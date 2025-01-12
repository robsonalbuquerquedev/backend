package br.edu.ifpe.manager.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaRequest {
	@NotNull
    private Long usuarioId;

    @NotNull
    private Long recursoId;

    @NotNull
    private LocalDateTime dataInicio;

    @NotNull
    private LocalDateTime dataFinal;
}
