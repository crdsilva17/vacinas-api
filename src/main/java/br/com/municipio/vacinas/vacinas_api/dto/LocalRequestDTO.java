package br.com.municipio.vacinas.vacinas_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LocalRequestDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String rua;
    private String numero;
    @NotBlank
    private String bairro;
    @NotBlank
    private String cidade;
    @NotBlank
    private String estado;
    @NotBlank
    private String cep;
    private String horarioFuncionamento;

}
