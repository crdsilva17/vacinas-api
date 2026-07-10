package br.com.municipio.vacinas.vacinas_api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.municipio.vacinas.vacinas_api.model.PasswordRecoveryCode;

public interface PasswordRecoveryCodeRepository extends MongoRepository<PasswordRecoveryCode, String> {
    Optional<PasswordRecoveryCode> findByEmailAndCode(String email, String code);
    void deleteByEmail(String email); // Limpa códigos antigos antes de gerar um novo
}

