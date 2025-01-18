package br.edu.ifpe.manager.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequest {

    private String nome;
    private String email;
    private String mensagem;

    // Getters and Setters
}