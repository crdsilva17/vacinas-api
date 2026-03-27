package br.com.municipio.vacinas.vacinas_api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "conteudos_educativos")  
public class ConteudoEducativo {

    @Id
    private String id;
    private String titulo;
    private String descricao;
    

}
