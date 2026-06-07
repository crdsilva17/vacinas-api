package br.com.municipio.vacinas.vacinas_api.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import lombok.*;

@Data
@Document("notifications")
public class Notification {

    @Id
    private String id;

    private String userId;

    private String title;

    private String message;

    private boolean read;

    private LocalDateTime createdAt;

}
