package br.com.municipio.vacinas.vacinas_api.dto;

import java.time.LocalDate;
import java.util.List;

public record CampanhaRequestDTO(
    String nome, 
    String vacinaId, 
    List<String> localIds, 
    LocalDate dataInicio, 
    LocalDate dataFim,
    String ageMin,
    String ageMax
) {
}
