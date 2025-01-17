package br.edu.ifpe.manager.service;

import br.edu.ifpe.manager.dto.RecursoDTO;
import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.StatusReserva;
import br.edu.ifpe.manager.repository.RecursoRepository;
import br.edu.ifpe.manager.request.RecursoRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecursoService {

	@Autowired
	private RecursoRepository recursoRepository;

	// Buscar recurso por ID e retornar um DTO
	public RecursoDTO buscarRecursoPorId(Long id) {
		Recurso recurso = recursoRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Recurso não encontrado com o ID: " + id));
		return toRecursoDTO(recurso);
	}

	// Listar todos os recursos como DTOs
	public List<RecursoDTO> listarRecursos() {
		return recursoRepository.findAll()
				.stream()
				.map(this::toRecursoDTO) // Converte cada Recurso para RecursoDTO
				.toList();
	}

	// Buscar recursos por nome e retornar como DTOs
	public List<RecursoDTO> buscarRecursosPorNome(String nome) {
		return recursoRepository.findByNomeContainingIgnoreCase(nome)
				.stream()
				.map(this::toRecursoDTO)
				.toList();
	}

	// Buscar recursos por localização e retornar como DTOs
	public List<RecursoDTO> buscarRecursosPorLocalizacao(String localizacao) {
		return recursoRepository.findByLocalizacao(localizacao)
				.stream()
				.map(this::toRecursoDTO)
				.toList();
	}

	// Buscar recursos por localização parcial e retornar como DTOs
	public List<RecursoDTO> buscarRecursosPorLocalizacaoParcial(String localizacao) {
		return recursoRepository.findByLocalizacaoContainingIgnoreCase(localizacao)
				.stream()
				.map(this::toRecursoDTO)
				.toList();
	}

	public RecursoDTO salvarRecurso(RecursoRequest recursoRequest) {
		// Se ID estiver presente, atualiza; caso contrário, cria um novo recurso
		Recurso recurso = recursoRequest.getId() != null 
				? recursoRepository.findById(recursoRequest.getId())
						.orElseThrow(() -> new IllegalArgumentException("Recurso não encontrado com o ID: " + recursoRequest.getId()))
						: new Recurso();

		// Copia os valores do RecursoRequest para a entidade
		recurso.setNome(recursoRequest.getNome());
		recurso.setDescricao(recursoRequest.getDescricao());
		recurso.setCapacidade(recursoRequest.getCapacidade());
		recurso.setLocalizacao(recursoRequest.getLocalizacao());

		// Definir o status do recurso como DISPONIVEL por padrão
		recurso.setStatus(StatusReserva.DISPONIVEL);

		// Salva no repositório
		Recurso recursoSalvo = recursoRepository.save(recurso);

		// Retorna como DTO
		return toRecursoDTO(recursoSalvo);
	}

	// Excluir recurso
	public void excluirRecurso(Long id) {
		if (!recursoRepository.existsById(id)) {
			throw new IllegalArgumentException("Recurso não encontrado com o ID: " + id);
		}
		recursoRepository.deleteById(id);
	}

	private RecursoDTO toRecursoDTO(Recurso recurso) {
		// Define o status do recurso com base nas reservas
		StatusReserva status = recurso.getReservas().stream()
				.filter(reserva -> reserva.getStatus() == StatusReserva.CONFIRMADA || reserva.getStatus() == StatusReserva.PENDENTE)
				.findFirst() // Busca a primeira reserva ativa
				.map(Reserva::getStatus) // Pega o status
				.orElse(StatusReserva.DISPONIVEL); // Caso não encontre, o recurso está DISPONÍVEL

		// Cria o DTO e inclui os IDs das reservas associadas
		List<Long> reservasIds = recurso.getReservas().stream()
				.map(Reserva::getId) // Mapeia para pegar os IDs das reservas
				.collect(Collectors.toList()); // Coleta os IDs em uma lista

		return new RecursoDTO(
				recurso.getId(),
				recurso.getNome(),
				recurso.getDescricao(),
				recurso.getCapacidade(),
				recurso.getLocalizacao(),
				status, // Retorna o status baseado nas reservas
				reservasIds // Adiciona os IDs das reservas associadas
				);
	}
}
