package br.com.municipio.vacinas.vacinas_api.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="lotes")
public class Lote {

    @Id
    private String id;

    @Indexed(unique = true)
    private String numeroLote;

    private List<String> fabricante;

    private List<String> vacinasAssociadas;

}
