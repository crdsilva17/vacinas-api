package br.com.municipio.vacinas.vacinas_api.repository;

import br.com.municipio.vacinas.vacinas_api.model.CampanhaVacinacao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CampanhaRepository extends MongoRepository<CampanhaVacinacao, String> {
    Optional<List<CampanhaVacinacao>> findByLocalIds(String localId);
    Optional<CampanhaVacinacao> findByAgeMinAndAgeMax(String ageMin, String ageMax);
    Optional<CampanhaVacinacao> findByVacinaId(String vacinaId);
    Optional<CampanhaVacinacao> findByNome(String nome);
}
