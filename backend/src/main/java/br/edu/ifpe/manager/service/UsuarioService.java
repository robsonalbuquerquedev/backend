package br.edu.ifpe.manager.service;

import br.edu.ifpe.manager.dto.UsuarioDTO;
import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.StatusReserva;
import br.edu.ifpe.manager.model.TipoUsuario;
import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import br.edu.ifpe.manager.repository.ReservaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    private final RecursoService recursoService;
    private final ReservaRepository reservaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RecursoService recursoService, ReservaRepository reservaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.recursoService = recursoService;
        this.reservaRepository = reservaRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + id));
    }

    // Novo método para buscar um usuário com suas reservas carregadas
    public Optional<Usuario> buscarUsuarioComReservas(Long id) {
        return usuarioRepository.findByIdWithReservas(id);
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
        Usuario usuarioExistente = buscarUsuarioPorId(id);

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
        Usuario usuario = buscarUsuarioPorId(id);
        logger.info("Deletando usuário com ID: " + id + " e email: " + usuario.getEmail());
        usuarioRepository.deleteById(id);
    }

    // Método para realizar uma reserva para o usuário
    public Reserva realizarReserva(Usuario usuario, Recurso recurso, LocalDateTime dataInicio, LocalDateTime dataFinal) {
        // Verifica se o recurso está disponível
        List<Recurso> recursosDisponiveis = recursoService.verificarDisponibilidade(dataInicio, dataFinal);
        if (!recursosDisponiveis.contains(recurso)) {
            throw new IllegalArgumentException("Recurso não disponível neste intervalo de tempo.");
        }

        // Cria a reserva
        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setRecurso(recurso);
        reserva.setDataInicio(dataInicio);
        reserva.setDataFinal(dataFinal);

     // Define o status baseado no tipo de usuário
        if (usuario.getTipo().equals(TipoUsuario.ALUNO)) {
            reserva.setStatus(StatusReserva.PENDENTE); // Usando o enum para o status
        } else {
            reserva.setStatus(StatusReserva.CONFIRMADA); // Usando o enum para o status
        }

        // Salva a reserva
        return reservaRepository.save(reserva);
    }

    // Conversão de Usuario para UsuarioDTO
    public UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getNome(), usuario.getTipo());
    }
}
