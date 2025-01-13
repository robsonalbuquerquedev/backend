package br.edu.ifpe.manager.service;

import br.edu.ifpe.manager.dto.RecursoRequest;
import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.repository.RecursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecursoService {

    @Autowired
    private RecursoRepository recursoRepository;

    // Buscar recurso por ID
    public Recurso buscarRecursoPorId(Long id) {
        return recursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recurso não encontrado com o ID: " + id));
    }

    // Listar todos os recursos
    public List<Recurso> listarRecursos() {
        return recursoRepository.findAll();
    }

    // Buscar recursos por nome
    public List<Recurso> buscarRecursosPorNome(String nome) {
        return recursoRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Buscar recursos por localização (exata)
    public List<Recurso> buscarRecursosPorLocalizacao(String localizacao) {
        return recursoRepository.findByLocalizacao(localizacao);
    }

    // Buscar recursos por localização (parcial)
    public List<Recurso> buscarRecursosPorLocalizacaoParcial(String localizacao) {
        return recursoRepository.findByLocalizacaoContainingIgnoreCase(localizacao);
    }

    // Salvar ou atualizar recurso com RecursoRequest
    public Recurso salvarRecurso(RecursoRequest recursoRequest) {
        // Se ID estiver presente, atualiza; caso contrário, cria um novo recurso
        Recurso recurso = recursoRequest.getId() != null 
                ? buscarRecursoPorId(recursoRequest.getId()) // Busca o recurso existente
                : new Recurso(); // Cria um novo recurso

        // Copia os valores do DTO para a entidade
        recurso.setNome(recursoRequest.getNome());
        recurso.setDescricao(recursoRequest.getDescricao());
        recurso.setCapacidade(recursoRequest.getCapacidade());
        recurso.setLocalizacao(recursoRequest.getLocalizacao());
        
        // Salva no repositório
        return recursoRepository.save(recurso);
    }

    // Excluir recurso
    public void excluirRecurso(Long id) {
        recursoRepository.deleteById(id);
    }
}
