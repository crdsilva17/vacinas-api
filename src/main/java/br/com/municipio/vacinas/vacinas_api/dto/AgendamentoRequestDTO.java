package br.com.municipio.vacinas.vacinas_api.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import br.com.municipio.vacinas.vacinas_api.model.enums.StatusAgendamento;

public record AgendamentoRequestDTO(
        String usuarioId,

        String vacinaId,

        String localId,

        LocalDate data,

        LocalTime horario,

        StatusAgendamento status) {

}
