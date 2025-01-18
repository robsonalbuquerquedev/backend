package br.edu.ifpe.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FeedbackDTO {

    private Long id;
    private String nome;
    private String email;
    private String mensagem;
}
