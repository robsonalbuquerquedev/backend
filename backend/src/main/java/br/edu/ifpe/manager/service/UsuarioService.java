package br.edu.ifpe.manager.service;

import br.edu.ifpe.manager.dto.UsuarioDTO;

import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import br.edu.ifpe.manager.request.UsuarioRequest;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
	private final UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	public boolean existeEmail(String email) {
		return usuarioRepository.findByEmail(email).isPresent();
	}

	public UsuarioDTO salvar(UsuarioRequest usuarioRequest) {
		// Validações automáticas via anotações no UsuarioRequest (@Valid deve ser usado no controlador)
		if (existeEmail(usuarioRequest.getEmail())) {
			throw new IllegalArgumentException("Email já está em uso.");
		}

		// Converte UsuarioRequest para Usuario utilizando o método toEntity
		Usuario novoUsuario = toEntity(usuarioRequest);

		// Salva no banco e retorna o DTO
		Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
		logger.info("Usuário salvo com sucesso: " + usuarioSalvo.getEmail());
		return toDTO(usuarioSalvo);
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

	public UsuarioDTO login(String email, String senha) {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas"));

		// Verificação simples de senha
		if (!senha.equals(usuario.getSenha())) {
			throw new IllegalArgumentException("Credenciais inválidas");
		}

		logger.info("Usuário logado com sucesso: " + email);
		return toDTO(usuario); // Retorna o DTO do usuário
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
