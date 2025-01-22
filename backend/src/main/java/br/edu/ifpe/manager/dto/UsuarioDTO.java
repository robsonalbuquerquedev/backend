package br.edu.ifpe.manager.dto;

import br.edu.ifpe.manager.model.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UsuarioDTO {
	private Long id;
	private String nome; 
	private String email;
	private TipoUsuario tipo;
	private boolean isApproved;
}
