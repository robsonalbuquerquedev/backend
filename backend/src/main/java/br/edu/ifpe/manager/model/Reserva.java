package br.edu.ifpe.manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O usuário é obrigatório.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotNull(message = "O recurso é obrigatório.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurso_id")
    private Recurso recurso;

    @NotNull(message = "A data e hora de início são obrigatórias.")
    private LocalDateTime dataHoraInicio;

    @NotNull(message = "A data e hora final são obrigatórias.")
    private LocalDateTime dataHoraFim;

    @Size(max = 255, message = "A descrição do recurso adicional não pode exceder 255 caracteres.")
    private String recursoAdicional;

    @NotNull(message = "O status da reserva é obrigatório.")
    @Enumerated(EnumType.STRING)
    private StatusReserva status;
}
