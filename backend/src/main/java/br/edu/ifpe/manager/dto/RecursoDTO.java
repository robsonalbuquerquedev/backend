package br.edu.ifpe.manager.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import br.edu.ifpe.manager.model.Laboratorio;
import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.RecursoAdicional;
import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.Sala;
import br.edu.ifpe.manager.model.StatusRecurso;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RecursoDTO {

    private Long id;  // Adicionando o campo id
    private String nome;
    private String descricao;
    private int capacidade;
    private StatusRecurso status;  // Enum StatusRecurso para o status do recurso

    @JsonProperty("tipoRecurso")
    private String tipoRecurso; // String para armazenar o tipo de recurso ("SALA" ou "LABORATORIO")

    // Método para converter tipoRecurso em uma instância concreta de Recurso (Sala ou Laboratorio)
    public Recurso toRecurso() {
        Recurso recurso;

        // Verifica o tipoRecurso para instanciar a classe apropriada
        if ("SALA".equalsIgnoreCase(this.tipoRecurso)) {
            recurso = new Sala();  // Instancia Sala
        } else if ("LABORATORIO".equalsIgnoreCase(this.tipoRecurso)) {
            recurso = new Laboratorio();  // Instancia Laboratorio
        } else {
            throw new IllegalArgumentException("Tipo de recurso inválido");
        }

        // Configura os dados comuns ao recurso
        recurso.setNome(this.nome);
        recurso.setDescricao(this.descricao);
        recurso.setCapacidade(this.capacidade);
        recurso.setStatus(this.status);

        return recurso;
    }
}
