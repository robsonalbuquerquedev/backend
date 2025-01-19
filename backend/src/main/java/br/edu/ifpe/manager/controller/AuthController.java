package br.edu.ifpe.manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.edu.ifpe.manager.dto.UsuarioDTO;
import br.edu.ifpe.manager.exception.ErrorResponse;
import br.edu.ifpe.manager.request.LoginRequest;
import br.edu.ifpe.manager.service.UsuarioService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UsuarioService usuarioService;

	public AuthController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		try {
			// Executa o login usando o serviço
			UsuarioDTO usuarioDTO = usuarioService.login(loginRequest.getEmail(), loginRequest.getSenha());

			// Retorna a resposta com os dados do usuário
			return ResponseEntity.ok(Map.of(
					"message", "Login realizado com sucesso",
					"usuario", usuarioDTO
					));
		} catch (IllegalArgumentException e) {
			// Trata erros relacionados a credenciais inválidas
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					new ErrorResponse(
							HttpStatus.UNAUTHORIZED.value(),
							"Credenciais inválidas",
							List.of("Email ou senha incorretos.")
							)
					);
		} catch (Exception e) {
			// Trata outros erros inesperados
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new ErrorResponse(
							HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Erro no login",
							List.of("Detalhes do erro: " + e.getMessage())
							)
					);
		}
	}
}
