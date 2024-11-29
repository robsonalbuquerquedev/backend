package br.edu.ifpe.manager.dto;

import lombok.Data;

@Data
public class RecursoAdicionalDTO {

    private Long id;           // Identificador único do recurso adicional
    private String nome;       // Nome do recurso adicional
    private String descricao;  // Descrição do recurso adicional
    private Integer quantidade; // Quantidade disponível ou necessária do recurso

}
