package br.com.municipio.vacinas.vacinas_api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.municipio.vacinas.vacinas_api.model.Vacina;

public interface VacinaRepository extends MongoRepository<Vacina, String> {

    boolean existsByNomeAndLote(String nome, String lote);

    List<Vacina> findByDataDisponivel(LocalDate dataDisponivel);
    List<Vacina> findByLocal(String Local);
    List<Vacina> findByDataDisponivelAndLocal(LocalDate dataDisponivel, String local);

}
