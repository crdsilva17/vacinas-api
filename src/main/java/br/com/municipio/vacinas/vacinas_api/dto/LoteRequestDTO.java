package br.com.municipio.vacinas.vacinas_api.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoteRequestDTO {

    @NotBlank
    private String numeroLote;

    private String tipo;
    @NotBlank
    private String fabricante;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate dataFabricacao;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate dataValidade;
    @NotNull
    private int quantidadeDisponivel;

}
