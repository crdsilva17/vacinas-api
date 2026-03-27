package br.com.municipio.vacinas.vacinas_api.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notificacoes")
public class Notificacao {

    @Id
    private String id;

    private String mensagem;
    private String usuarioId;
    private Date dataEnvio;

}
