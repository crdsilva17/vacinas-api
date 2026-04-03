package br.com.municipio.vacinas.vacinas_api.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoteResponseDTO {

    private String id;

    @NotBlank
    private String numeroLote;

    private String tipo;
    private String fabricante;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFabricacao;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataValidade;

}
