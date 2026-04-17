package br.com.municipio.vacinas.vacinas_api.repository;

import br.com.municipio.vacinas.vacinas_api.model.CampanhaVacinacao;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CampanhaRepository extends MongoRepository<CampanhaVacinacao, String> {
    CampanhaVacinacao findByLocalId(String localId);
}
