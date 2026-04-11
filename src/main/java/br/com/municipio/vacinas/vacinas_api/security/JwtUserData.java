package br.com.municipio.vacinas.vacinas_api.security;

import lombok.Builder;

@Builder
public record JwtUserData(String userId, String email) {

}
