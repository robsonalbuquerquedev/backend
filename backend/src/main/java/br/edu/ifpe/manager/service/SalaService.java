package br.edu.ifpe.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpe.manager.model.Sala;
import br.edu.ifpe.manager.model.StatusSala;
import br.edu.ifpe.manager.repository.SalaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    public List<Sala> listarTodos() {
        return salaRepository.findAll();
    }

    public List<Sala> listarPorStatus(StatusSala status) {
        return salaRepository.findByStatus(status);
    }

    public Optional<Sala> buscarPorId(Long id) {
        return salaRepository.findById(id);
    }

    public Sala salvar(Sala sala) {
        return salaRepository.save(sala);
    }

    public void deletarPorId(Long id) {
        salaRepository.deleteById(id);
    }
}
