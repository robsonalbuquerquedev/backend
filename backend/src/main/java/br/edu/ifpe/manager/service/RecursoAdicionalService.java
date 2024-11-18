package br.edu.ifpe.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpe.manager.dto.RecursoAdicionalDTO;
import br.edu.ifpe.manager.model.RecursoAdicional;
import br.edu.ifpe.manager.repository.RecursoAdicionalRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecursoAdicionalService {

    @Autowired
    private RecursoAdicionalRepository recursoAdicionalRepository;

    // Listar todos os recursos adicionais (agora retorna uma lista de DTOs)
    public List<RecursoAdicionalDTO> listarTodos() {
        List<RecursoAdicional> recursosAdicionais = recursoAdicionalRepository.findAll();
        return recursosAdicionais.stream()
                                 .map(this::converterParaDTO)
                                 .collect(Collectors.toList());
    }

    // Buscar recurso adicional por ID (agora retorna DTO)
    public Optional<RecursoAdicionalDTO> buscarPorId(Long id) {
        Optional<RecursoAdicional> recursoAdicional = recursoAdicionalRepository.findById(id);
        return recursoAdicional.map(this::converterParaDTO);
    }

    // Salvar ou atualizar recurso adicional (aceita DTO e retorna DTO)
    public RecursoAdicionalDTO salvar(RecursoAdicionalDTO recursoAdicionalDTO) {
        RecursoAdicional recursoAdicional = converterParaEntidade(recursoAdicionalDTO);
        RecursoAdicional novoRecurso = recursoAdicionalRepository.save(recursoAdicional);
        return converterParaDTO(novoRecurso);
    }

    // Deletar recurso adicional por ID
    public void deletar(Long id) {
        recursoAdicionalRepository.deleteById(id);
    }

    // Conversão de RecursoAdicional para RecursoAdicionalDTO
    private RecursoAdicionalDTO converterParaDTO(RecursoAdicional recursoAdicional) {
        RecursoAdicionalDTO dto = new RecursoAdicionalDTO();
        dto.setId(recursoAdicional.getId());
        dto.setNome(recursoAdicional.getNome());
        dto.setDescricao(recursoAdicional.getDescricao());
        return dto;
    }

    // Conversão de RecursoAdicionalDTO para RecursoAdicional
    private RecursoAdicional converterParaEntidade(RecursoAdicionalDTO dto) {
        RecursoAdicional recursoAdicional = new RecursoAdicional();
        recursoAdicional.setId(dto.getId());
        recursoAdicional.setNome(dto.getNome());
        recursoAdicional.setDescricao(dto.getDescricao());
        return recursoAdicional;
    }
}
