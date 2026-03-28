package br.com.municipio.vacinas.vacinas_api.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "conteudos_educativos")  
public class ConteudoEducativo {

    @Id
    private UUID id;
    private String titulo;
    private String descricao;
    private Date dataPublicacao;
    private Date dataAtualizacao;
    

}
