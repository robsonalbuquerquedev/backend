package br.edu.ifpe.manager.dto;

import br.edu.ifpe.manager.model.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class UsuarioDTO {
    private Long id;        // Adicionado para expor o ID do usuário
    private String nome;    // Nome do usuário
    private TipoUsuario tipo; // Tipo do usuário (Coordenador, Professor, Aluno)
}
