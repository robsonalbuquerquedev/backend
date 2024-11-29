package br.edu.ifpe.manager.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecursoDTO {

    private Long id;

    private String nome;

    private String descricao;

    private int capacidade;

    private String status; // Representado como String para facilitar o uso no front-end

    private String localizacao; // Campo para localização

    private List<Long> reservasIds; // IDs das reservas associadas
}
