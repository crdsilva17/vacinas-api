package br.com.municipio.vacinas.vacinas_api.dto;

import lombok.Data;

@Data
public class LocalRequestDTO {

    private String name;
    private String enderecoId;
    private String horarioFuncionamento;

}
