package br.com.municipio.vacinas.vacinas_api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.municipio.vacinas.vacinas_api.model.Usuario;


public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);

    Optional<UserDetails> findByCpf(String cpf);
    Optional<UserDetails> findByEmail(String email);

}
