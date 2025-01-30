package br.edu.ifpe.manager.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FeedbackRequest {

    private String nome;
    private String email;
    private String mensagem;

    // Getters and Setters
}