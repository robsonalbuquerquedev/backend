package br.edu.ifpe.manager.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RecursoRequest {

    private Long id;

    @NotEmpty(message = "O nome do recurso não pode estar vazio.")
    private String nome;

    @NotEmpty(message = "A descrição do recurso não pode estar vazia.")
    private String descricao;

    @Min(value = 1, message = "A capacidade do recurso deve ser maior que 0.")
    private int capacidade;

    @NotEmpty(message = "A localização do recurso não pode estar vazia.")
    private String localizacao;
}
