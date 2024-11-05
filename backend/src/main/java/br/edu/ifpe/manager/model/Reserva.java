package br.edu.ifpe.manager.model;

import lombok.Data;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = true)
    private Sala sala;

    @ManyToOne
    @JoinColumn(name = "laboratorio_id", nullable = true)
    private Laboratorio laboratorio;

    @ManyToOne
    @JoinColumn(name = "recurso_id", nullable = true)
    private Recurso recursoAdicional;
}
