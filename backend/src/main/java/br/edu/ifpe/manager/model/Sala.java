package br.edu.ifpe.manager.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("SALA")  // Especifica que este é o tipo "Sala" na tabela única
public class Sala extends Recurso {

    // A classe Sala herda os campos da classe Recurso
    // Outros campos ou comportamentos específicos de sala podem ser adicionados, se necessário.
}
