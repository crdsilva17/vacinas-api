package br.com.municipio.vacinas.vacinas_api.repository;

import br.com.municipio.vacinas.vacinas_api.model.Lote;

public interface LoteRepository extends org.springframework.data.mongodb.repository.MongoRepository<Lote, String> {
    
    boolean existsByNumeroLote(String numeroLote);

}
