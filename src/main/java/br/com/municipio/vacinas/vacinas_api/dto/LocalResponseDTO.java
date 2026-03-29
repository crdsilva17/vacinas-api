package br.com.municipio.vacinas.vacinas_api.dto;

import lombok.Data;

@Data
public class LocalResponseDTO {

    private String id;
    private String name;
    private String enderecoId;
    
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    private String horarioFuncionamento;

}
