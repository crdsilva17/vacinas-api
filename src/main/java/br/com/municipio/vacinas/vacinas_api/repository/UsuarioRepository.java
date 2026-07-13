package br.com.municipio.vacinas.vacinas_api.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import br.com.municipio.vacinas.vacinas_api.model.Usuario;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    Optional<UserDetails> findByCpf(String cpf);

    Optional<UserDetails> findByEmail(String email);

    Optional<Usuario> findById(String id);

    Optional<Usuario> findUserByEmail(String email);

    List<Usuario> findByDataNsctoBetween(LocalDate dataInicial, LocalDate dataFinal);

    List<Usuario> findByLocalIdIn(List<String> localIds);

}
