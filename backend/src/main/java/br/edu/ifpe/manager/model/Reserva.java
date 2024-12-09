package br.edu.ifpe.manager.model;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Data
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Validação para garantir que dataInicio não seja nula e seja uma data no presente ou futura
    @NotNull(message = "A data de início não pode ser nula.")
    @FutureOrPresent(message = "A data de início deve ser no presente ou no futuro.")
    @Column(nullable = false)
    private LocalDateTime dataInicio;

    // Validação para garantir que dataFim não seja nula e seja posterior a dataInicio
    @NotNull(message = "A data de fim não pode ser nula.")
    @Column(nullable = false)
    private LocalDateTime dataFim;

    // Relacionamento com a entidade Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull(message = "O usuário é obrigatório.")
    private Usuario usuario;

    // Relacionamento com a entidade Recurso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurso_id", nullable = false)
    @NotNull(message = "O recurso é obrigatório.")
    private Recurso recurso;

    // Validação de recurso adicional com tamanho máximo
    @Size(max = 255, message = "O recurso adicional não pode exceder 255 caracteres.")
    @Column(length = 255)
    private String recursoAdicional;

    // Validação para garantir que dataFim seja posterior a dataInicio
    @PrePersist
    @PreUpdate
    public void validarDatas() {
        if (dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException("A data de fim deve ser posterior à data de início.");
        }
    }
}
