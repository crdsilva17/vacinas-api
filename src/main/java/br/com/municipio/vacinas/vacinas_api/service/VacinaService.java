package br.com.municipio.vacinas.vacinas_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mongodb.DuplicateKeyException;

import java.time.LocalDate;

import br.com.municipio.vacinas.vacinas_api.repository.VacinaRepository;
import lombok.AllArgsConstructor;
import br.com.municipio.vacinas.vacinas_api.model.Vacina;

@Service
@AllArgsConstructor
public class VacinaService {

    private final VacinaRepository repository;

    public Vacina cadastrarVacina(Vacina vacina) {

        if (repository.existsByNomeAndLote(vacina.getNome(), vacina.getLote())) {
            throw new RuntimeException("Já existe uma vacina com esse nome e lote!");
        }
        try {
            return repository.save(vacina);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Vacina com mesmo nome e lote já existe!");
        }
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

    public List<Vacina> filtrar(LocalDate data, String local) {
        return repository.findByDataDisponivelAndLocal(data, local);
    }

}
