package br.com.municipio.vacinas.vacinas_api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.municipio.vacinas.vacinas_api.model.Vacina;

import java.time.LocalDate;
import java.util.List;



public interface VacinaRepository extends MongoRepository<Vacina, String> {
    
    boolean existsByNomeAndLoteAndLocalAndFabricanteAndDataFabricacao(String nome, String lote, String local, String fabricante, LocalDate dataFabricacao);

    List<Vacina> findByLocal(String local);
    List<Vacina> findByLote(String lote);
    List<Vacina> findByNome(String nome);
    List<Vacina> findAllByFabricante(String fabricante);

}
