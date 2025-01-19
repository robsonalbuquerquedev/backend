package br.edu.ifpe.manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.edu.ifpe.manager.dto.UsuarioDTO;
import br.edu.ifpe.manager.exception.ErrorResponse;
import br.edu.ifpe.manager.request.UsuarioRequest;
import br.edu.ifpe.manager.service.UsuarioService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@PostMapping
	public ResponseEntity<?> salvar(@RequestBody @Valid UsuarioRequest usuarioRequest) {
		if (usuarioService.existeEmail(usuarioRequest.getEmail())) {
			// Retorno consistente de erro com ErrorResponse
			return ResponseEntity.badRequest()
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "O email já está em uso.", List.of("Email: " + usuarioRequest.getEmail())));
		}

		UsuarioDTO novoUsuarioDTO = usuarioService.salvar(usuarioRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuarioDTO);
	}

}
