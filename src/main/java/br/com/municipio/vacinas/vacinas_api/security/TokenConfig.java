package br.com.municipio.vacinas.vacinas_api.security;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.municipio.vacinas.vacinas_api.model.Usuario;

@Component
public class TokenConfig {
        // Configurações relacionadas à geração e validação de tokens JWT
        private String secretKey = "sua_chave_secreta_aqui";
        private long expirationTime = 86400000; // 1 dia em milissegundos

        public String generateToken(Usuario usuario) {
            // Implementar lógica para gerar um token JWT usando as informações do usuário
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            
            return JWT.create()
                .withClaim("userId", usuario.getId())
                .withSubject(usuario.getEmail())
                .withExpiresAt(Instant.now().plusMillis(expirationTime))
                .withIssuedAt(Instant.now())
                .sign(algorithm)
            ; // Retornar o token gerado

        }

        public Optional<JwtUserData> validateToken(String token) {
            // Implementar lógica para validar o token JWT
            try {
                Algorithm algorithm = Algorithm.HMAC256(secretKey);
                DecodedJWT decode = JWT.require(algorithm).build().verify(token);
                return Optional.of(JwtUserData.builder()
                .userId(decode.getClaim("userId").asString())
                .email(decode.getSubject())
                .build()
                );

            } catch (JWTVerificationException e) {
                return Optional.empty();
               
            }
        }
}
