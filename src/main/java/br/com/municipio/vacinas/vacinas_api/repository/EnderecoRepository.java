package br.com.municipio.vacinas.vacinas_api.repository;

import java.util.Optional;

import br.com.municipio.vacinas.vacinas_api.model.Endereco;

public interface EnderecoRepository  extends org.springframework.data.mongodb.repository.MongoRepository<Endereco, String> {

    Optional<Endereco> findByLocalId(String localId);

}
