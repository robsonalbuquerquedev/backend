package br.edu.ifpe.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.repository.ReservaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    // Método para buscar todas as reservas
    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    // Método para buscar reserva por ID
    public Optional<Reserva> buscarReservaPorId(Long id) {
        return reservaRepository.findById(id);
    }

    // Método para criar ou atualizar reserva
    public Reserva criarReserva(Reserva reserva) {
        // Adicione qualquer lógica adicional de validação ou processamento antes de salvar
        return reservaRepository.save(reserva);
    }

    // Método para excluir reserva
    public void excluirReserva(Long id) {
        reservaRepository.deleteById(id);
    }
}
