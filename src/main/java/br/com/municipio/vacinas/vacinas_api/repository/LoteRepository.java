package br.com.municipio.vacinas.vacinas_api.repository;

import org.springframework.transaction.annotation.Transactional;

import br.com.municipio.vacinas.vacinas_api.model.Lote;

import java.util.List;
import java.util.Optional;


public interface LoteRepository extends org.springframework.data.mongodb.repository.MongoRepository<Lote, String> {
    
    boolean existsByNumeroLote(String numeroLote);
    Optional<List<Lote>> findByNumeroLote(String numeroLote);

    @Transactional
    void deleteByNumeroLote(String numeroLote);

}
