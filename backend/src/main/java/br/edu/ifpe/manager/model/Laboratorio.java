package br.edu.ifpe.manager.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
public class Laboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;
    
    private int capacidade;

    @Enumerated(EnumType.STRING)
    private StatusLaboratorio status;

    @OneToMany(mappedBy = "laboratorio", cascade = CascadeType.ALL)
    private List<Reserva> reservas;
}
