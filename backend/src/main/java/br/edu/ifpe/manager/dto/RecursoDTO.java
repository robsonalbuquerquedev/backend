package br.edu.ifpe.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecursoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private int capacidade;
    private String localizacao;
}
