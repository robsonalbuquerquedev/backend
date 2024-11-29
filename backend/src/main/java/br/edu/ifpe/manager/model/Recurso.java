package br.edu.ifpe.manager.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private StatusRecurso status;

    // Campo para localização
    private String localizacao;

    // Relação com reservas
    @OneToMany(mappedBy = "recurso", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    // Métodos utilitários para sincronizar relações bidirecionais
    public void adicionarReserva(Reserva reserva) {
        reservas.add(reserva);
        reserva.setRecurso(this);
    }

    public void removerReserva(Reserva reserva) {
        reservas.remove(reserva);
        reserva.setRecurso(null);
    }
}
