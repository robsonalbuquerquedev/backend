package br.edu.ifpe.manager.dto;

import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.StatusReserva;
import br.edu.ifpe.manager.model.Usuario;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
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
