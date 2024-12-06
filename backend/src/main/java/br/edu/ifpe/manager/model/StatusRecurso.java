package br.edu.ifpe.manager.model;

public enum StatusRecurso {
    DISPONIVEL,   // Recurso disponível para reserva
    INDISPONIVEL, // Recurso indisponível (por exemplo, em manutenção)
    RESERVADO;    // Recurso reservado
}
