package br.edu.ifpe.manager.service;

import br.edu.ifpe.manager.dto.UsuarioDTO;

import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import br.edu.ifpe.manager.request.UsuarioRequest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
	private final UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	public boolean existeEmail(String email) {
		return usuarioRepository.findByEmail(email) != null;  // Verifica se UserDetails não é null
	}
	
	public List<UsuarioDTO> listarTodos() {
		List<Usuario> usuarios = usuarioRepository.findAll();
		return usuarios.stream()
				.map(usuario -> new UsuarioDTO(
						usuario.getId(),
						usuario.getNome(),
						usuario.getEmail(),
						usuario.getTipo()
						))
				.collect(Collectors.toList());
	}
	
	// Conversão de Usuario para UsuarioDTO
	private UsuarioDTO toDTO(Usuario usuario) {
		return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getTipo());
	}

	// Conversão de UsuarioRequest para Usuario
	private Usuario toEntity(UsuarioRequest usuarioRequest) {
		Usuario usuario = new Usuario();
		usuario.setNome(usuarioRequest.getNome());
		usuario.setEmail(usuarioRequest.getEmail());
		usuario.setSenha(usuarioRequest.getSenha());
		usuario.setTipo(usuarioRequest.getTipo());
		return usuario;
	}
}
