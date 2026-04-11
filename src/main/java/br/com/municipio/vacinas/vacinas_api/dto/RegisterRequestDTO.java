package br.com.municipio.vacinas.vacinas_api.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import br.com.municipio.vacinas.vacinas_api.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterRequestDTO {
    
    @NotBlank
    private String localId;
    @NotBlank
    private String nome;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String senha;
    @NotNull
    private LocalDate dataNscto;
    @NotBlank
    @CPF
    private String cpf;
    @NotNull
    private Role role;
}
