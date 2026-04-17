package br.com.municipio.vacinas.vacinas_api.dto;

public record CampanhaResponseDTO(String id, String nome, String vacinaId,
                                  String localId, String dataInicio, String dataFim) {
}
