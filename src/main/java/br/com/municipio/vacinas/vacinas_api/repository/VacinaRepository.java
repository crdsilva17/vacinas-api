package br.com.municipio.vacinas.vacinas_api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.municipio.vacinas.vacinas_api.model.Vacina;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface VacinaRepository extends MongoRepository<Vacina, String> {
    
    boolean existsByNomeAndLoteAndLocalIdAndFabricanteAndDataFabricacao(String nome, String lote, String localId,
                                                                        String fabricante, LocalDate dataFabricacao);

    List<Vacina> findByLocalId(String localId);
    List<Vacina> findByLote(String lote);
    List<Vacina> findByNome(String nome);
    List<Vacina> findByFabricante(String fabricante);

}
