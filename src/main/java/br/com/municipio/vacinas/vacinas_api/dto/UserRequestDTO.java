package br.com.municipio.vacinas.vacinas_api.dto;

import java.time.LocalDate;

public record UserRequestDTO(String email, String nome, String local, LocalDate dataNscto) {}
