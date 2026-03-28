package br.com.municipio.vacinas.vacinas_api.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notificacoes")
public class Notificacao {

    @Id
    private UUID id;
    
    private String mensagem;

    @Field(name="user_id")
    private UUID usuarioId;
    @Field(name="data_envio")
    private Date dataEnvio;

}
