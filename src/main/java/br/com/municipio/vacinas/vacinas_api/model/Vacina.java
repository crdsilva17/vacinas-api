package br.com.municipio.vacinas.vacinas_api.model;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

@CompoundIndexes({
    @CompoundIndex(name = "nome_lote_unique", def = "{'nome': 1, 'lote': 1}", unique = true)
})

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="vacinas") 
public class Vacina {
    
    @Id
    private String id;

    private String local;
    private String nome;
    private String descricao;
    private String fabricante;
    private String lote;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFabricacao;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataValidade;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataDisponivel;

    private int idadeMinima;
    private int idadeMaxima;
    private int doses;
    private int quantidadeDisponivel;

}
