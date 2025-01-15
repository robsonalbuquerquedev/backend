package br.edu.ifpe.manager.service;

import br.edu.ifpe.manager.dto.UsuarioDTO;

import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import br.edu.ifpe.manager.request.UsuarioRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioDTO> listarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(this::toDTO) // Converte cada Usuario para UsuarioDTO
                .toList();
    }

    public UsuarioDTO buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + id));
        return toDTO(usuario); // Converte para DTO antes de retornar
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

    public UsuarioDTO atualizar(Long id, UsuarioRequest usuarioRequest) {
        Usuario usuarioExistente = buscarUsuarioPorIdEntidade(id);

        // Verifica se o e-mail está sendo alterado e se já existe
        if (!usuarioExistente.getEmail().equals(usuarioRequest.getEmail()) && existeEmail(usuarioRequest.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso.");
        }

        // Converte UsuarioRequest para Usuario utilizando o método toEntity
        Usuario usuarioAtualizado = toEntity(usuarioRequest);

        // Atualiza o usuário existente com os dados fornecidos
        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setSenha(usuarioAtualizado.getSenha());
        usuarioExistente.setTipo(usuarioAtualizado.getTipo());

        Usuario usuarioAtualizadoSalvo = usuarioRepository.save(usuarioExistente);
        return toDTO(usuarioAtualizadoSalvo);
    }
    
    public void deletarPorId(Long id) {
        Usuario usuario = buscarUsuarioPorIdEntidade(id);
        logger.info("Deletando usuário com ID: " + id + " e email: " + usuario.getEmail());
        usuarioRepository.deleteById(id);
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
        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getTipo());
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

    // Método privado para buscar entidade (interno)
    private Usuario buscarUsuarioPorIdEntidade(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + id));
    }
}
