package br.com.municipio.vacinas.vacinas_api.service;

import java.util.List;

import br.com.municipio.vacinas.vacinas_api.repository.VaccineRepository;
import br.com.municipio.vacinas.vacinas_api.model.Vacina;

public class GerenciadorVacinas {

    private final VaccineRepository repository;

    public GerenciadorVacinas(VaccineRepository repository) {
        this.repository = repository;
    }

    public Vacina cadastrarVacina(Vacina vacina) {
        return repository.save(vacina);
    }

    public Vacina editarVacina(Vacina vacina) {
        return repository.save(vacina);
    }

    public void excluirVacina(String id) {
        repository.deleteById(id);
    }

    public List<Vacina> buscarVacinas() {
        return repository.findAll();
    }

}
