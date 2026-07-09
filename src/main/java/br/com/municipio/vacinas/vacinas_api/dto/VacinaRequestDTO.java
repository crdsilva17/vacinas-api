package br.com.municipio.vacinas.vacinas_api.dto;

import java.time.LocalDate;

import br.com.municipio.vacinas.vacinas_api.model.enums.Doses;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VacinaRequestDTO {

    @NotBlank(message = "O campo lote é obrigatório")
    private String lote;
    @NotBlank(message = "O campo nome é obrigatório")
    private String nome;

    @NotNull(message = "O campo doses é obrigatório")
    private Doses doses;

    @NotBlank(message = "O campo descrição é obrigatório")
    private String descricao;
    @NotBlank(message = "O campo fabricante é obrigatório")
    private String fabricante;
    @NotNull(message = "O campo data de fabricação é obrigatório")
    private LocalDate dataFabricacao;
    @NotNull(message = "O campo data de validade é obrigatório")
    private LocalDate dataValidade;
    
}
