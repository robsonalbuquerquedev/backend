package br.edu.ifpe.manager.dto;

import br.edu.ifpe.manager.model.TipoUsuario;

public record RegisterDTO(String nome, String email, String senha, TipoUsuario tipo) {
}