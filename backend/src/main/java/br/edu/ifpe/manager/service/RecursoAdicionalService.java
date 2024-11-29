package br.edu.ifpe.manager.service;

import br.edu.ifpe.manager.model.RecursoAdicional;
import br.edu.ifpe.manager.repository.RecursoAdicionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecursoAdicionalService {

    @Autowired
    private RecursoAdicionalRepository recursoAdicionalRepository;

    // Método para salvar ou atualizar um recurso adicional
    public RecursoAdicional salvarRecursoAdicional(RecursoAdicional recursoAdicional) {
        return recursoAdicionalRepository.save(recursoAdicional);
    }

    // Método para buscar um recurso adicional por ID
    public Optional<RecursoAdicional> buscarRecursoAdicionalPorId(Long id) {
        return recursoAdicionalRepository.findById(id);
    }

    // Método para listar todos os recursos adicionais
    public List<RecursoAdicional> listarRecursosAdicionais() {
        return recursoAdicionalRepository.findAll(); // Retorna todos os recursos adicionais
    }

    // Método para listar recursos adicionais por nome
    public List<RecursoAdicional> listarRecursosAdicionaisPorNome(String nome) {
        return recursoAdicionalRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Método para listar recursos adicionais por quantidade
    public List<RecursoAdicional> listarRecursosAdicionaisPorQuantidade(Integer quantidade) {
        return recursoAdicionalRepository.findByQuantidadeGreaterThanEqual(quantidade);
    }

    // Método para excluir um recurso adicional
    public void excluirRecursoAdicional(Long id) {
        recursoAdicionalRepository.deleteById(id);
    }
}
