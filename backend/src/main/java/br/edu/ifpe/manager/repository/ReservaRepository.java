package br.edu.ifpe.manager.repository;

import br.edu.ifpe.manager.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Consulta para verificar conflitos de reserva com base em intervalos de datas
    List<Reserva> findByRecursoIdAndDataInicioBeforeAndDataFimAfter(Long recursoId, LocalDateTime dataFim, LocalDateTime dataInicio);

    // Consulta personalizada para encontrar reservas por usuário
    List<Reserva> findByUsuarioId(Long usuarioId);

    // Consulta personalizada para encontrar reservas por recurso
    List<Reserva> findByRecursoId(Long recursoId);

    // Caso queira buscar por recurso adicional, agora o campo é uma String
    List<Reserva> findByRecursoAdicionalContaining(String recursoAdicional);  // Permite buscar por parte do nome do recurso adicional
}
