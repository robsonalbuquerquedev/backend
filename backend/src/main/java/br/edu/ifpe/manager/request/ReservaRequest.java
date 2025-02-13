package br.edu.ifpe.manager.request;

import br.edu.ifpe.manager.model.StatusReserva;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequest {

    @NotNull(message = "A data de início é obrigatória.")
    private LocalDateTime dataInicio;

    @NotNull(message = "A data de fim é obrigatória.")
    private LocalDateTime dataFim;

    private String recursoAdicional;

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Long usuarioId;

    @NotNull(message = "O ID do recurso é obrigatório.")
    private Long recursoId;

    private StatusReserva status;
}
