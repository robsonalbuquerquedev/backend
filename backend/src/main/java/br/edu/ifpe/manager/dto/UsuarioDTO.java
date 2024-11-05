package br.edu.ifpe.manager.dto;

import br.edu.ifpe.manager.model.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioDTO {
    private String nome;
    private TipoUsuario tipo;
}
