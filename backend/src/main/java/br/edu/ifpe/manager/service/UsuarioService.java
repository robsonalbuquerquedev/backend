package br.edu.ifpe.manager.service;

import br.edu.ifpe.manager.dto.UsuarioDTO;
import br.edu.ifpe.manager.infra.security.TokenService;
import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import br.edu.ifpe.manager.request.UsuarioRequest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {
	private final UsuarioRepository usuarioRepository;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private PasswordEncoder passwordEncoder;

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

	public void processarEsqueceuSenha(String email) {
		// Verifica se o usuário existe
		UserDetails userDetails = usuarioRepository.findByEmail(email);
		if (userDetails == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
		}

		// Converte UserDetails para Usuario
		Usuario usuario = (Usuario) userDetails;

		// Gerar o token usando o TokenService
		String token = tokenService.generateToken(usuario);

		// Construir o link de redefinição
		String linkRedefinicao = "http://localhost:5173/redefinir-senha?token=" + token;

		// Enviar o email com o link
		enviarEmailRedefinicao(usuario, linkRedefinicao);
	}

	public void redefinirSenha(String token, String novaSenha) {
		// Validar o token usando o TokenService
		String email = tokenService.validateToken(token);
		if (email.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido ou expirado");
		}

		// Buscar o usuário
		UserDetails userDetails = usuarioRepository.findByEmail(email);
		if (userDetails == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
		}

		// Converte UserDetails para Usuario
		Usuario usuario = (Usuario) userDetails;

		// Atualizar a senha
		usuario.setSenha(passwordEncoder.encode(novaSenha));
		usuarioRepository.save(usuario);
	}
	
	public boolean validarToken(String token) {
	    String email = tokenService.validateToken(token);
	    return !email.isEmpty();  // Retorna true se o token for válido
	}
	
	private void enviarEmailRedefinicao(Usuario usuario, String linkRedefinicao) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(usuario.getEmail());
			message.setSubject("Redefinição de Senha");

			message.setText(
					"Olá " + usuario.getNome() + ",\n\n" +
							"Recebemos uma solicitação para redefinir sua senha. Para prosseguir, clique no link abaixo:\n\n" +
							linkRedefinicao + "\n\n" +
							"Se você não solicitou a redefinição, ignore este email.\n\n" +
							"Atenciosamente,\n" +
							"Equipe do Sistema"
					);

			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Falha ao enviar o email de redefinição de senha: " + e.getMessage());
		}
	}
}
