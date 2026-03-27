package br.com.municipio.vacinas.vacinas_api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="vacinas") 
public class Vacina {
    
    @Id
    private String id;

    private String nome;
    private String descricao;
    private String faixaEtaria;
    private int doses;

}
