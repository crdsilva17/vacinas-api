package br.com.municipio.vacinas.vacinas_api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.municipio.vacinas.vacinas_api.model.Usuario;

import java.util.UUID;


public interface UsuarioRepository extends MongoRepository<Usuario, UUID> {

}
