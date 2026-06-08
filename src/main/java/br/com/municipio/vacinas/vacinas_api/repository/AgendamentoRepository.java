package br.com.municipio.vacinas.vacinas_api.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.municipio.vacinas.vacinas_api.model.Agendamento;
import br.com.municipio.vacinas.vacinas_api.model.enums.StatusAgendamento;

@Repository
public interface AgendamentoRepository
        extends MongoRepository<Agendamento, String> {

    List<Agendamento> findByLocalIdAndData(
            String localId,
            LocalDate data);

    boolean existsByLocalIdAndDataAndHorarioAndStatus(
            String localId,
            LocalDate data,
            LocalTime horario,
            StatusAgendamento status);

    List<Agendamento> findByUsuarioId(
            String usuarioId);
}
