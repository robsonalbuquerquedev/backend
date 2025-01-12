package br.edu.ifpe.manager.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data de início é obrigatória.")
    @FutureOrPresent(message = "A data de início deve ser hoje ou uma data futura.")
    private LocalDateTime dataInicio;

    @NotNull(message = "A data final é obrigatória.")
    @FutureOrPresent(message = "A data final deve ser hoje ou uma data futura.")
    private LocalDateTime dataFinal;

    @NotNull(message = "O usuário que fez a reserva é obrigatório.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull(message = "O recurso a ser reservado é obrigatório.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurso_id", nullable = false)
    private Recurso recurso;

    @Column(name = "recurso_adicional")
    private String recursoAdicional;

    @NotNull(message = "O status da reserva é obrigatório.")
    @Enumerated(EnumType.STRING) // Armazena como texto no banco de dados
    private StatusReserva status;
}
