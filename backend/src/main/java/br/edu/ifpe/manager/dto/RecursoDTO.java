package br.edu.ifpe.manager.dto;
import java.util.List;

import br.edu.ifpe.manager.model.StatusReserva;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecursoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private int capacidade;
    private String localizacao;
    private StatusReserva status;
    
 // Inclui os IDs das reservas associadas
    private List<Long> reservasIds;
}
