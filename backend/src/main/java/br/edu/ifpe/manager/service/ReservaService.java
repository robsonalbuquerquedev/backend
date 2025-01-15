package br.edu.ifpe.manager.service;

import br.edu.ifpe.manager.model.Reserva;
import br.edu.ifpe.manager.model.Usuario;
import br.edu.ifpe.manager.model.Recurso;
import br.edu.ifpe.manager.repository.ReservaRepository;
import br.edu.ifpe.manager.repository.UsuarioRepository;
import br.edu.ifpe.manager.request.ReservaRequest;
import br.edu.ifpe.manager.repository.RecursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

	private final ReservaRepository reservaRepository;
	private final UsuarioRepository usuarioRepository;
	private final RecursoRepository recursoRepository;

	public ReservaService(ReservaRepository reservaRepository, 
			UsuarioRepository usuarioRepository,
			RecursoRepository recursoRepository) {
		this.reservaRepository = reservaRepository;
		this.usuarioRepository = usuarioRepository;
		this.recursoRepository = recursoRepository;
	}

	public List<Reserva> listarTodas() {
		return reservaRepository.findAll();
	}

	public Reserva criarReserva(ReservaRequest request) {
		try {
			Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
					.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + request.getUsuarioId()));

			Recurso recurso = recursoRepository.findById(request.getRecursoId())
					.orElseThrow(() -> new IllegalArgumentException("Recurso não encontrado com ID: " + request.getRecursoId()));

			Reserva reserva = new Reserva();
			reserva.setDataInicio(request.getDataInicio());
			reserva.setDataFim(request.getDataFim());
			reserva.setRecursoAdicional(request.getRecursoAdicional());
			reserva.setStatus(request.getStatus());
			reserva.setUsuario(usuario);
			reserva.setRecurso(recurso);

			return reservaRepository.save(reserva);
		} catch (Exception e) {
			e.printStackTrace();  // Imprime o erro no console
			throw new RuntimeException("Erro ao criar reserva: " + e.getMessage(), e);  // Lança uma exceção mais detalhada
		}
	}
	
	public Reserva atualizarReserva(Long id, ReservaRequest request) {
		Reserva reserva = reservaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com ID: " + id));

		reserva.setDataInicio(request.getDataInicio());
		reserva.setDataFim(request.getDataFim());
		reserva.setRecursoAdicional(request.getRecursoAdicional());
		reserva.setStatus(request.getStatus());

		return reservaRepository.save(reserva);
	}

	public void deletarReserva(Long id) {
		Reserva reserva = reservaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com ID: " + id));
		reservaRepository.delete(reserva);
	}
}
