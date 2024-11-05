package br.edu.ifpe.manager.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;
    
    private int capacidade;

    @Enumerated(EnumType.STRING)
    private StatusSala status;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL)
    private List<Reserva> reservas;
}
