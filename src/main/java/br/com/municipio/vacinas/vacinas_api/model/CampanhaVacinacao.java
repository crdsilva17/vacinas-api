package br.com.municipio.vacinas.vacinas_api.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

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
    private List<String> localIds;
    @NonNull
    private LocalDate dataInicio;
    @NonNull
    private LocalDate dataFim;
    private String ageMin;
    private String ageMax;
}
