package br.edu.ifpe.manager.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
		var auth = this.authenticationManager.authenticate(usernamePassword);

		// Recupera o usuário autenticado
		var usuario = (Usuario) auth.getPrincipal();

		// Gera o token JWT
		var token = tokenService.generateToken(usuario);

		// Retorna o token e os dados do usuário
		var responseDTO = new LoginResponseDTO(
				token,
				usuario.getId(),
				usuario.getNome(),
				usuario.getEmail(),
				usuario.getTipo()
				);

		return ResponseEntity.ok(responseDTO);
	}

	@PostMapping("/register")
	public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
		if(this.repository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "O email já está em uso.", List.of("Email: " + data.email())));

		String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
		Usuario newUser = new Usuario(data.nome(), data.email(), encryptedPassword, data.tipo());

		this.repository.save(newUser);

		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}
}
