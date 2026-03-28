package br.com.municipio.vacinas.vacinas_api.dto;

import java.time.LocalDate;

import br.com.municipio.vacinas.vacinas_api.model.enums.Doses;
import lombok.Data;

@Data
public class VacinaResponseDTO {

    private String id;
    private String local;
    private String lote;

    private String nome;
    private int idadeMinima;
    private int idadeMaxima;
    private Doses doses;

    private String descricao;
    private String fabricante;
    private LocalDate dataFabricacao;
    private LocalDate dataValidade;
    private int quantidadeDisponivel;

}
