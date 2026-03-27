package br.com.municipio.vacinas.vacinas_api.model;

import br.com.municipio.vacinas.vacinas_api.model.enums.Role;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="usuarios")
public class Usuario {

    @Id
    private String id;

    private String nome;
    private String email;
    private String senha;
    private Date dataNscto;

    private Role role;

}
