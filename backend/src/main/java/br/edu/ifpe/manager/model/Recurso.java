package br.edu.ifpe.manager.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "O nome do recurso não pode estar vazio.")
    private String nome;

    @NotEmpty(message = "A descrição do recurso não pode estar vazia.")
    private String descricao;

    @Min(value = 1, message = "A capacidade do recurso deve ser maior que 0.")
    private int capacidade;

    @NotEmpty(message = "A localização do recurso não pode estar vazia.")
    private String localizacao;
    
    @ManyToMany(mappedBy = "recursosReservados")
    private Set<Usuario> usuarios = new HashSet<>();
    
    @OneToMany(mappedBy = "recurso", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reserva> reservas = new HashSet<>();
}
