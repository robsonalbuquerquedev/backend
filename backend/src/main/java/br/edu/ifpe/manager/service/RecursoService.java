package br.edu.ifpe.manager.service;

import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.StatusRecurso;
import br.edu.ifpe.manager.repository.RecursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecursoService {

    @Autowired
    private RecursoRepository recursoRepository;
    
    public Recurso buscarRecursoPorId(Long id) {
    	return recursoRepository.findById(id)
    			.orElseThrow(() -> new IllegalArgumentException("Recurso não encontrado com o ID: " + id));
    }

    // Método para listar todos os recursos
    public List<Recurso> listarRecursos() {
        return recursoRepository.findAll();
    }

    // Método para buscar recursos por status
    public List<Recurso> buscarRecursosPorStatus(StatusRecurso status) {
        return recursoRepository.findByStatus(status);
    }

    // Método para buscar recursos por nome
    public List<Recurso> buscarRecursosPorNome(String nome) {
        return recursoRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Método para buscar recursos por localização (exata)
    public List<Recurso> buscarRecursosPorLocalizacao(String localizacao) {
        return recursoRepository.findByLocalizacao(localizacao);
    }

    // Método para buscar recursos por localização (busca parcial)
    public List<Recurso> buscarRecursosPorLocalizacaoParcial(String localizacao) {
        return recursoRepository.findByLocalizacaoContainingIgnoreCase(localizacao);
    }

    // Método para salvar ou atualizar um recurso
    public Recurso salvarRecurso(Recurso recurso) {
        return recursoRepository.save(recurso);
    }

    // Método para excluir um recurso
    public void excluirRecurso(Long id) {
        recursoRepository.deleteById(id);
    }
}
