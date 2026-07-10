package br.com.municipio.vacinas.vacinas_api.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "password_recovery_codes")
public class PasswordRecoveryCode {
    @Id
    private String id;
    @Indexed // Ajuda na busca rápida
    private String email;
    private String code;
    
    @Indexed(expireAfter = "900s") // 900 segundos = 15 minutos (O Mongo apaga o registro sozinho após esse tempo)
    private LocalDateTime createdAt;

    // Construtores, Getters e Setters
    public PasswordRecoveryCode(String email, String code) {
        this.email = email;
        this.code = code;
        this.createdAt = LocalDateTime.now();
    }
}

