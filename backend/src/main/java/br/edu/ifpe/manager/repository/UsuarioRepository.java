package br.edu.ifpe.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifpe.manager.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    
 // Busca um usuário com suas reservas carregadas
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.reservas WHERE u.id = :id")
    Optional<Usuario> findByIdWithReservas(@Param("id") Long id);
}
