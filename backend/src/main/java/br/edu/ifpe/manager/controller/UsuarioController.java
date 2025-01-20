package br.edu.ifpe.manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifpe.manager.dto.UsuarioDTO;
import br.edu.ifpe.manager.service.UsuarioService;

import java.util.List;
import java.util.Map;

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
	
	@PostMapping("/esqueceu-senha")
    public ResponseEntity<?> esqueceuSenha(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        usuarioService.processarEsqueceuSenha(email);
        return ResponseEntity.ok("Se o email estiver cadastrado, você receberá um link para redefinir sua senha.");
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<?> redefinirSenha(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String novaSenha = request.get("novaSenha");
        usuarioService.redefinirSenha(token, novaSenha);
        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }
    
    @GetMapping("/redefinir-senha")
    public ResponseEntity<String> exibirMensagemToken(@RequestParam("token") String token) {
        // Validar o token no serviço
        if (!usuarioService.validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou expirado.");
        }

        // Caso o token seja válido
        return ResponseEntity.ok("Token válido. Você pode redefinir sua senha agora.");
    }
}
