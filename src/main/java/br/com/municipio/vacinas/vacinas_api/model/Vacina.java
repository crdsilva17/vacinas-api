package br.com.municipio.vacinas.vacinas_api.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.municipio.vacinas.vacinas_api.model.enums.Doses;
import lombok.*;

@CompoundIndexes({
    @CompoundIndex(name = "nome_loteId_localId_unique", def = "{'nome': 1, 'loteId': 1, 'localId': 1}", unique = true)
})

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="vacinas") 
public class Vacina {
    
    @Id
    private String id;

    private String local;
    private String lote;
    private String nome;

    private int idadeMinima;
    private int idadeMaxima;
    private Doses doses;

    private String descricao;
    private String fabricante;
    private LocalDate dataFabricacao;
    private LocalDate dataValidade;
    private int quantidadeDisponivel;
}
