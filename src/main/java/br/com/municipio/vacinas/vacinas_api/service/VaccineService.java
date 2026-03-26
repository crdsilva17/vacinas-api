package br.com.municipio.vacinas.vacinas_api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.municipio.vacinas.vacinas_api.entity.VaccineEntity;
import br.com.municipio.vacinas.vacinas_api.repository.VaccineRepository;

@Service
public class VaccineService {

    private final VaccineRepository repository;

    public VaccineService(VaccineRepository repository) {
        this.repository = repository;
    }

    public List<VaccineEntity> listarTodas() {
        return repository.findAll();
    }

    public List<VaccineEntity> filtrar(LocalDate data, String localId) {
        return repository.findByDataDisponivelAndLocalId(data, localId);

    }

    public List<VaccineEntity> filtrarlocal(String localId) {
        return repository.findByLocalId(localId);
    }

    public VaccineEntity salvar(VaccineEntity vaccine) {
        return repository.save(vaccine);
        
    }

    public void delete(VaccineEntity vaccine) {
        repository.delete(vaccine);
    }

}
