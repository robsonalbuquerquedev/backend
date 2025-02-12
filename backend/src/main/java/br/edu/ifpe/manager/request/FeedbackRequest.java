package br.edu.ifpe.manager.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {

	@NotBlank(message = "O nome não pode estar vazio")
    private String nome;
	@NotBlank(message = "O e-mail não pode estar vazio")
    @Email(message = "Formato de e-mail inválido")
    private String email;
    @NotBlank(message = "A mensagem não pode estar vazia")
    private String mensagem;

    // Getters and Setters
}