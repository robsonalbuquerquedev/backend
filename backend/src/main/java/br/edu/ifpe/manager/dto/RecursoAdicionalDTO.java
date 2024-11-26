package br.edu.ifpe.manager.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecursoAdicionalDTO {

    private Long id;

    @NotNull(message = "O nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Size(max = 255, message = "A descrição pode ter no máximo 255 caracteres")
    private String descricao;

    // Alteração: Recurso não precisa ser id, mas sim a referência direta.
    // Isso torna a estrutura mais orientada a objetos e elimina a necessidade de um campo extra
    private RecursoDTO recurso;  // Associando um DTO de Recurso diretamente ao DTO de Recurso Adicional
}
