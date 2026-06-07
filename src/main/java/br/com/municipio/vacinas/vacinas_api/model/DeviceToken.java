package br.com.municipio.vacinas.vacinas_api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("device_tokens")
public class DeviceToken {

    @Id
    private String id;

    private String userId;

    private String token;

}