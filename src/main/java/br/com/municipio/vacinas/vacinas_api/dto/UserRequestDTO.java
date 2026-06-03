package br.com.municipio.vacinas.vacinas_api.dto;

import java.time.LocalDate;

public record UserRequestDTO(String localId, String nome, LocalDate dataNscto) {}
