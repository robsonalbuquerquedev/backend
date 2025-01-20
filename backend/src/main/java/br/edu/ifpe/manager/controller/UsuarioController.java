package br.edu.ifpe.manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifpe.manager.dto.UsuarioDTO;
import br.edu.ifpe.manager.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	private final UsuarioService usuarioService;
	
	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@GetMapping
	public ResponseEntity<List<UsuarioDTO>> listarTodos() {
	    List<UsuarioDTO> usuarios = usuarioService.listarTodos();
	    return ResponseEntity.ok(usuarios);
	}
}
