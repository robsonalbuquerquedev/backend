package br.edu.ifpe.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpe.manager.model.RecursoAdicional;
import br.edu.ifpe.manager.repository.RecursoAdicionalRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecursoAdicionalService {

    @Autowired
    private RecursoAdicionalRepository recursoAdicionalRepository;

    // Método para listar todos os recursos adicionais
    public List<RecursoAdicional> listarTodos() {
        return recursoAdicionalRepository.findAll();
    }

    // Método para buscar recurso adicional por ID
    public Optional<RecursoAdicional> buscarPorId(Long id) {
        return recursoAdicionalRepository.findById(id);
    }

    // Método para salvar ou atualizar um recurso adicional
    public RecursoAdicional salvarRecursoAdicional(RecursoAdicional recursoAdicional) {
        return recursoAdicionalRepository.save(recursoAdicional);
    }

    // Método para deletar recurso adicional por ID
    public void deletarPorId(Long id) {
        recursoAdicionalRepository.deleteById(id);
    }
}
