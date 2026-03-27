package br.com.municipio.vacinas.vacinas_api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "locais")
public class LocalVacina {

    @Id
    private String id;
    
    private String name;
    private String endereco;
    private String horarioFuncionamento;

}
