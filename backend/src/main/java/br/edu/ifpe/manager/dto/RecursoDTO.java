package br.edu.ifpe.manager.dto;

import lombok.Data;

@Data
public class RecursoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private int capacidade;
    private String localizacao; // Campo para localização
}
