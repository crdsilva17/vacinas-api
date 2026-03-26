package br.com.municipio.vacinas.vacinas_api.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "local_entity")
public class LocalEntity {

    @Id
    private String id;
    private String name;
    private String cepString;
    private String street;
    private String number;
    private List <VaccineEntity> vaccines;

}
