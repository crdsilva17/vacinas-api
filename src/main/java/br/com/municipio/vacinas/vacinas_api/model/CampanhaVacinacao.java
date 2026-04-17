package br.com.municipio.vacinas.vacinas_api.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "campanhas_vacinacao")
public class CampanhaVacinacao {
    @Id
    private String id;
    @NonNull
    private String nome;
    @NonNull
    private String vacinaId;
    @NonNull
    private String localId;
    @NonNull
    private LocalDate dataInicio;
    @NonNull
    private LocalDate dataFim;
}
