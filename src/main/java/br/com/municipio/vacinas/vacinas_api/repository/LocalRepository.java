package br.com.municipio.vacinas.vacinas_api.repository;

import org.springframework.transaction.annotation.Transactional;

import br.com.municipio.vacinas.vacinas_api.model.LocalVacina;

public interface LocalRepository extends org.springframework.data.mongodb.repository.MongoRepository<LocalVacina, String> {
    
    boolean existsByName(String name);

    @Transactional
    void deleteByName(String name);

}
