package br.edu.ifpe.manager.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpe.manager.dto.AuthenticationDTO;
import br.edu.ifpe.manager.dto.LoginResponseDTO;
import br.edu.ifpe.manager.dto.RegisterDTO;
import br.edu.ifpe.manager.exception.ErrorResponse;
import br.edu.ifpe.manager.infra.security.TokenService;
import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import br.edu.ifpe.manager.service.EmailService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private TokenService tokenService;
	
	@Autowired
    private EmailService emailService;
	
	@GetMapping("/approve/{id}")
	public ResponseEntity<?> approveUser(@PathVariable Long id) {
		Optional<Usuario> userOpt = this.repository.findById(id);
		if (userOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
		}

		Usuario user = userOpt.get();
		if (user.isApproved()) {
			return ResponseEntity.badRequest().body("O usuário já foi aprovado.");
		}

		user.setApproved(true);
		this.repository.save(user);

		// Enviar email ao usuário informando que ele foi aprovado
		emailService.enviarEmailAprovacaoParaUsuario(user);

		return ResponseEntity.ok("Usuário aprovado com sucesso.");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
		// Busca o usuário pelo email
		UserDetails userDetails = this.repository.findByEmail(data.email());
		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos.");
		}

		// Converte UserDetails para Usuario
		Usuario user = (Usuario) userDetails;

		// Verifica se a senha está correta
		if (!new BCryptPasswordEncoder().matches(data.senha(), user.getSenha())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos.");
		}

		// Verifica se o usuário foi aprovado
		if (!user.isApproved()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Seu cadastro ainda não foi aprovado.");
		}

		// Autentica o usuário e gera o token JWT
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
		var auth = this.authenticationManager.authenticate(usernamePassword);

		// Recupera o usuário autenticado
		var authenticatedUser = (Usuario) auth.getPrincipal();

		// Gera o token JWT
		String token = tokenService.generateToken(authenticatedUser);

		// Retorna o token e os dados do usuário
		var responseDTO = new LoginResponseDTO(
				token,
				authenticatedUser.getId(),
				authenticatedUser.getNome(),
				authenticatedUser.getEmail(),
				authenticatedUser.getTipo()
				);

		return ResponseEntity.ok(responseDTO);
	}

	@PostMapping("/register")
	public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
		if(this.repository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "O email já está em uso.", List.of("Email: " + data.email())));

		String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
		Usuario newUser = new Usuario(data.nome(), data.email(), encryptedPassword, data.tipo(), false);

		this.repository.save(newUser);

		// Enviar email para o ADMIN com link para aprovação
		String approvalLink = "http://localhost:5173/approve/" + newUser.getId();

		emailService.enviarEmailAprovacao(newUser, approvalLink);

		return ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado e aguardando aprovação.");
	}
}
