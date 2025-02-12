package br.edu.ifpe.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeedbackDTO {

    private Long id;
    private String nome;
    private String email;
    private String mensagem;
}
