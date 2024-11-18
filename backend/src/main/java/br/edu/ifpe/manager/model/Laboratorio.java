package br.edu.ifpe.manager.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("LABORATORIO")  // Especifica que este é o tipo "Laboratório" na tabela única
public class Laboratorio extends Recurso {

    // A classe Laboratorio herda os campos da classe Recurso
    // Outros campos ou comportamentos específicos de laboratório podem ser adicionados, se necessário.
}
