package br.com.municipio.vacinas.vacinas_api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.municipio.vacinas.vacinas_api.model.enums.StatusAgendamento;
import lombok.Data;

@Data
@Document(collection = "agendamentos")
public class Agendamento {

    @Id
    private String id;

    private String usuarioId;

    private String vacinaId;

    private String localId;

    private LocalDate data;

    private LocalTime horario;

    private StatusAgendamento status;

    private LocalDateTime createdAt;
}