package br.edu.ifpe.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.repository.RecursoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecursoService {

    @Autowired
    private RecursoRepository recursoRepository;

    public List<Recurso> listarTodos() {
        return recursoRepository.findAll();
    }

    public Optional<Recurso> buscarPorId(Long id) {
        return recursoRepository.findById(id);
    }

    public Recurso salvar(Recurso recurso) {
        return recursoRepository.save(recurso);
    }

    public void deletarPorId(Long id) {
        recursoRepository.deleteById(id);
    }
}
