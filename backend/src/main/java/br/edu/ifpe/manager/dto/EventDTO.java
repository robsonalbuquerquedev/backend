package br.edu.ifpe.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class EventDTO {
	private int id;
    private String date;
    private String description;
}
