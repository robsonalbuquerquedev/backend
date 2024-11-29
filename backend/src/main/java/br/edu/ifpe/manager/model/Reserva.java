package br.edu.ifpe.manager.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataInicio;

    @Column(nullable = false)
    private LocalDateTime dataFim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurso_id", nullable = false)
    private Recurso recurso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurso_adicional_id")
    private RecursoAdicional recursoAdicional;

    // Validação para garantir que dataFim seja posterior a dataInicio
    @PrePersist
    @PreUpdate
    public void validarDatas() {
        if (dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException("A data de fim deve ser posterior à data de início.");
        }
    }
}
