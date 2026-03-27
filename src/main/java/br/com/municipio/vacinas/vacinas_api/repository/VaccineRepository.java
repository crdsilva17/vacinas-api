package br.com.municipio.vacinas.vacinas_api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.municipio.vacinas.vacinas_api.model.Vacina;

public interface VaccineRepository extends MongoRepository<Vacina, String> {

    List<Vacina> findByDataDisponivel(LocalDate dataDisponivel);
    List<Vacina> findByLocalId(String LocalId);
    List<Vacina> findByDataDisponivelAndLocalId(LocalDate dataDisponivel, String localId);

}
