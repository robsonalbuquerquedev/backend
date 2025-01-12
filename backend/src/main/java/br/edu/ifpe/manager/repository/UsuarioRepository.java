package br.edu.ifpe.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.manager.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

}
