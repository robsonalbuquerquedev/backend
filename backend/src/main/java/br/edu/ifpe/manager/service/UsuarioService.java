package br.edu.ifpe.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.edu.ifpe.manager.dto.UsuarioDTO;
import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository; // Injeção via construtor
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + id));
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public boolean existeEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    public Usuario salvar(Usuario usuario) {
        // Validações
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio.");
        }
        if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia.");
        }

        // Verifica se o email já está em uso
        if (existeEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso.");
        }

        try {
            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            logger.info("Usuário salvo com sucesso: " + usuario.getEmail());
            return usuarioSalvo;
        } catch (Exception e) {
            logger.error("Erro ao salvar o usuário: " + usuario.getEmail(), e);
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage());
        }
    }

    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = buscarPorId(id);

        // Verifica se o e-mail está sendo alterado e se já existe
        if (!usuarioExistente.getEmail().equals(usuarioAtualizado.getEmail())) {
            Optional<Usuario> emailJaExiste = usuarioRepository.findByEmail(usuarioAtualizado.getEmail());
            if (emailJaExiste.isPresent()) {
                throw new IllegalArgumentException("Email já está em uso.");
            }
        }

        // Atualiza os campos do usuário
        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());

        // Atualiza a senha, se fornecida
        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
            usuarioExistente.setSenha(usuarioAtualizado.getSenha()); // Mantenha a senha sem criptografia
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public Usuario login(String email, String senha) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            // Verificação simples de senha
            if (senha.equals(usuario.getSenha())) {
                logger.info("Usuário logado com sucesso: " + email);
                return usuario; // Retornar o usuário em vez de um token
            }
        }
        throw new IllegalArgumentException("Credenciais inválidas");
    }

    public void deletarPorId(Long id) {
        Usuario usuario = buscarPorId(id);
        logger.info("Deletando usuário com ID: " + id + " e email: " + usuario.getEmail());
        usuarioRepository.deleteById(id);
    }

    // Conversão de Usuario para UsuarioDTO
    public UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getNome(), usuario.getTipo());
    }
}
