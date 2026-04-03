package br.com.municipio.vacinas.vacinas_api.dto;

import java.util.Date;

import org.hibernate.validator.constraints.br.CPF;

import br.com.municipio.vacinas.vacinas_api.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequestDTO {
    
    @NotBlank
    private String localId;
    @NotBlank
    private String nome;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String senha;
    @NotBlank
    private Date dataNscto;
    @NotBlank
    @CPF
    private String cpf;
    @NotBlank
    private Role role;
}
