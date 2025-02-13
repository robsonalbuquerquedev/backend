package br.edu.ifpe.manager.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Usuario implements UserDetails{

	private static final long serialVersionUID = 1L; // Adicionado para resolver o aviso do serialVersionUID

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome é obrigatório.")
	@Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
	private String nome;

	@NotBlank(message = "O e-mail é obrigatório.")
	@Email(message = "O e-mail informado é inválido.")
	@Column(unique = true)
	private String email;

	@NotBlank(message = "A senha é obrigatória.")
	@Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
	private String senha;

	@NotNull(message = "O tipo de usuário é obrigatório.")
	@Enumerated(EnumType.STRING)
	private TipoUsuario tipo;
	
	private boolean isApproved = false;

	@ManyToMany
	@JoinTable(
			name = "usuario_recurso",
			joinColumns = @JoinColumn(name = "usuario_id"),
			inverseJoinColumns = @JoinColumn(name = "recurso_id")
			)
	private List<Recurso> recursosReservados = new ArrayList<>();

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Reserva> reservas = new ArrayList<>();
	
    public Usuario(String nome, String email, String senha, TipoUsuario tipo, boolean isApproved) {
    	this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
        this.isApproved = isApproved;
    }
    
    public Usuario(Long id, String nome, String email, TipoUsuario tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Retorne as permissões ou roles do usuário
		if(this.tipo == TipoUsuario.COORDENADOR) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
		else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getPassword() {
		// Senha armazenada no objeto
		return senha;
	}

	@Override
	public String getUsername() {
		// Pode usar o e-mail como username
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// Retornar true para indicar que a conta não expirou
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// Retornar true para indicar que a conta não está bloqueada
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// Retornar true para indicar que as credenciais não expiraram
		return true;
	}

	@Override
	public boolean isEnabled() {
		// Retornar true para indicar que o usuário está ativo
		return true;
	}
}
