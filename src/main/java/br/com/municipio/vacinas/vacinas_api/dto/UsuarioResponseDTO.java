package br.com.municipio.vacinas.vacinas_api.dto;

import java.util.Date;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.municipio.vacinas.vacinas_api.model.enums.Role;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UsuarioResponseDTO {

    private String id;
    private String localId;
    private String nome;
    @Email
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dataNscto;
    @CPF
    private String cpf;
    private Role role;

}
