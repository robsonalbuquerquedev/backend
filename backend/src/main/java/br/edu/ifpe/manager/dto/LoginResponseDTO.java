package br.edu.ifpe.manager.dto;

import br.edu.ifpe.manager.model.TipoUsuario;

public record LoginResponseDTO(String token, Long id, String nome, String email, TipoUsuario tipo) {
}