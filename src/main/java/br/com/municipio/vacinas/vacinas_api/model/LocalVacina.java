package br.com.municipio.vacinas.vacinas_api.model;

import lombok.*;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "locais")
public class LocalVacina {

    @Id
    private UUID id;
    @Indexed(unique = true)
    private String name;
    private Endereco endereco;
    private String horarioFuncionamento;

}
