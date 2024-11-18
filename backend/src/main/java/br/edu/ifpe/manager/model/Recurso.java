package br.edu.ifpe.manager.model;

import jakarta.persistence.*;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_recurso", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    private int capacidade;

    @Enumerated(EnumType.STRING)
    private StatusRecurso status;  // Status geral de recurso (sala ou laboratório)

    @OneToMany(mappedBy = "recurso", cascade = CascadeType.ALL)
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "recurso", cascade = CascadeType.ALL)
    private List<RecursoAdicional> recursosAdicionais;  // Recurso adicional (como datashow)
}
