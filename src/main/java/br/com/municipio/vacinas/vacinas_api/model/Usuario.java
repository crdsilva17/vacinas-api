package br.com.municipio.vacinas.vacinas_api.model;

import br.com.municipio.vacinas.vacinas_api.model.enums.Role;

import org.springframework.data.annotation.Id;

import lombok.*;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="usuarios")
public class Usuario {

    @Id
    private UUID id;

    @Field(name="full_name")
    private String nome;

    @Indexed(unique = true)
    private String email;

    @Field(name="password")
    private String senha;

    @Field(name="birth_date")
    private Date dataNscto;

    @Field(name="role")
    private Role role;

}
