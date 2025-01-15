package br.edu.ifpe.manager.dto;

import java.time.LocalDateTime;

import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.StatusReserva;
import lombok.Data;

@Data
public class ReservaDTO {

    private Long id;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String recursoAdicional;
    private StatusReserva status;

    // Informações relacionadas ao usuário (apenas o nome, por exemplo)
    private String nomeUsuario;

    // Informações relacionadas ao recurso (apenas o nome, por exemplo)
    private String nomeRecurso;

    public ReservaDTO(Reserva reserva) {
        this.id = reserva.getId();
        this.dataInicio = reserva.getDataInicio();
        this.dataFim = reserva.getDataFim();
        this.recursoAdicional = reserva.getRecursoAdicional();
        this.status = reserva.getStatus();
        this.nomeUsuario = reserva.getUsuario().getNome(); 
        this.nomeRecurso = reserva.getRecurso().getNome();
    }
}
