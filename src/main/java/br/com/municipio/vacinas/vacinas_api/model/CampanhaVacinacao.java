package br.com.municipio.vacinas.vacinas_api.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "campanhas_vacinacao")
public class CampanhaVacinacao {
    @Id
    private String id;
    private String nome;
    private Vacina vacina;
    private String localId;
    private LocalDate dataInicio;
    private LocalDate dataFim;
}
