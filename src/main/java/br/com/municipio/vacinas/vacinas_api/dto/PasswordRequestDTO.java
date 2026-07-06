package br.com.municipio.vacinas.vacinas_api.dto;

public record PasswordRequestDTO(String email, String oldPassword, String newPassword) {

}
