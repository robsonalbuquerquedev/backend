package br.edu.ifpe.manager.model;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório.")  // Valida se o nome não está vazio ou nulo
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")  // Valida se o email não está vazio
    @Email(message = "O e-mail informado é inválido.")  // Valida se o e-mail tem formato correto
    @Column(unique = true)
    private String email;

    @NotBlank(message = "A senha é obrigatória.")  // Valida se a senha não está vazia
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")  // Valida o tamanho da senha
    private String senha;

    @NotNull(message = "O tipo de usuário é obrigatório.")  // Valida se o tipo não é nulo
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;
}
