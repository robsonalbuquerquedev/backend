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

    // Agora, ao invés de ter sala e laboratório como campos separados, usamos apenas 'recurso'
    @ManyToOne
    @JoinColumn(name = "recurso_id", nullable = true)
    private Recurso recurso;  // Este campo pode ser uma Sala ou um Laboratório (ambos herdam de Recurso)

    @ManyToOne
    @JoinColumn(name = "recurso_adicional_id", nullable = true)
    private RecursoAdicional recursoAdicional;  // Relacionamento com recursos adicionais, como projetores
}
