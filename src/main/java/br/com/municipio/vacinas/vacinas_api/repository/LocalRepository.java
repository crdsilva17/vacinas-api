package br.com.municipio.vacinas.vacinas_api.repository;

import br.com.municipio.vacinas.vacinas_api.model.LocalVacina;

public interface LocalRepository extends org.springframework.data.mongodb.repository.MongoRepository<LocalVacina, String> {
    
    boolean existsByName(String name);  

}
