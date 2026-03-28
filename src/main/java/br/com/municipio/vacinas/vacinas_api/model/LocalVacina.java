package br.com.municipio.vacinas.vacinas_api.model;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "locais")
public class LocalVacina {

    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private String enderecoId;
    private String horarioFuncionamento;

}
