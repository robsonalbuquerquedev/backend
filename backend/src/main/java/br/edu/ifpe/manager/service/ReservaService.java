package br.edu.ifpe.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.repository.ReservaRepository;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public Reserva criarReserva(Reserva reserva) {
        // O Spring JPA já vai associar automaticamente o Usuario e Recurso se forem passados corretamente na requisição
        return reservaRepository.save(reserva);
    }
}
