package br.com.municipio.vacinas.vacinas_api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.municipio.vacinas.vacinas_api.entity.VaccineEntity;

public interface VaccineRepository extends MongoRepository<VaccineEntity, String> {

    List<VaccineEntity> findByDataDisponivel(LocalDate dataDisponivel);
    List<VaccineEntity> findByLocalId(String LocalId);
    List<VaccineEntity> findByDataDisponivelAndLocalId(LocalDate dataDisponivel, String localId);

}
