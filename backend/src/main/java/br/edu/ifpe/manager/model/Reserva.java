package br.edu.ifpe.manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data e hora de início não podem ser nulas")
    @FutureOrPresent(message = "A data e hora de início deve ser no presente ou no futuro")
    private LocalDateTime startDate;

    @NotNull(message = "A data e hora de término não podem ser nulas")
    @FutureOrPresent(message = "A data e hora de término deve ser no presente ou no futuro")
    private LocalDateTime endDate;

    @AssertTrue(message = "A data e hora de término não pode ser anterior à data de início")
    public boolean isEndDateAfterStartDate() {
        return endDate == null || startDate == null || !endDate.isBefore(startDate);
    }

    private boolean includeAdditionalResource;

    @Size(max = 255, message = "A descrição do recurso adicional não pode ter mais de 255 caracteres")
    private String additionalResource;

    @NotNull(message = "O recurso deve ser válido")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurso_id")
    private Recurso recurso;

    @NotNull(message = "O usuário deve ser válido")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
