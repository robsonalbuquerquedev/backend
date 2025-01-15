package br.edu.ifpe.manager.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data e hora de início são obrigatórias.")
    private LocalDateTime dataInicio;

    @NotNull(message = "A data e hora do fim são obrigatórias.")
    private LocalDateTime dataFim;

    @Size(max = 255, message = "A descrição do recurso adicional não pode exceder 255 caracteres.")
    private String recursoAdicional;

    // Relacionamento com Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relacionamento com Recurso
    @ManyToOne
    @JoinColumn(name = "recurso_id", nullable = false)
    private Recurso recurso;
    
 // Status da Reserva
    @Enumerated(EnumType.STRING)
    @NotNull(message = "O status da reserva é obrigatório.")
    private StatusReserva status = StatusReserva.PENDENTE; // Valor padrão
}
