package br.com.municipio.vacinas.vacinas_api.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mongodb.DuplicateKeyException;

import lombok.RequiredArgsConstructor;

import br.com.municipio.vacinas.vacinas_api.repository.LocalRepository;
import br.com.municipio.vacinas.vacinas_api.repository.LoteRepository;

import br.com.municipio.vacinas.vacinas_api.model.Vacina;
import br.com.municipio.vacinas.vacinas_api.mapper.VacinaMapper;
import br.com.municipio.vacinas.vacinas_api.dto.VacinaRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.VacinaResponseDTO;
import br.com.municipio.vacinas.vacinas_api.repository.VacinaRepository;

import br.com.municipio.vacinas.vacinas_api.model.Lote;

@Service
@RequiredArgsConstructor
public class VacinaService {

    private final VacinaRepository repository;
    private final LocalRepository localRepository;
    private final LoteRepository loteRepository;
    private final VacinaMapper mapper;

    public VacinaResponseDTO cadastrarVacina(VacinaRequestDTO request) {

        if (repository.existsByNomeAndLoteAndLocalAndFabricanteAndDataFabricacao(
                request.getNome(), request.getLote(), request.getLocal(), request.getFabricante(), request.getDataFabricacao())) {
            throw new RuntimeException("Já existe uma vacina com esse nome, lote e fabricante para esse Posto de Saúde!");
        }
        try {

            if (!localRepository.existsByName(request.getLocal())) {

                throw new RuntimeException("Posto de Saúde não encontrado! Por favor, cadastre o Posto de Saúde antes de cadastrar a vacina.");
            }

            if (!loteRepository.existsByNumeroLote(request.getLote())) {
                
                Lote lote = new Lote();
                lote.setNumeroLote(request.getLote());
                lote.setFabricante(Arrays.asList(request.getFabricante()));
                lote.setTipo(request.getDescricao());
                lote.setVacinasAssociadas(Arrays.asList(request.getNome()));
                loteRepository.save(lote);  
            } else {
                Lote lote = loteRepository.findByNumeroLote(request.getLote()).orElseThrow(null);
                List<String> vacinasAssociadas = lote.getVacinasAssociadas();
                List<String> fabricantes = lote.getFabricante();
                if (!vacinasAssociadas.contains(request.getNome())) {
                    vacinasAssociadas.add(request.getNome());
                    lote.setVacinasAssociadas(vacinasAssociadas);
                    
                }
                if (!fabricantes.contains(request.getFabricante())) {
                    fabricantes.add(request.getFabricante());
                    lote.setFabricante(fabricantes);
                }
                loteRepository.save(lote);
            }

            return mapper.toDTO(repository.save(mapper.toEntity(request)));

        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Vacina com mesmo nome e lote já existe!");
        }
    }

    public VacinaResponseDTO editarVacina(VacinaRequestDTO request) {
        return mapper.toDTO(repository.save(mapper.toEntity(request)));
    }

    public void excluirVacinaPorId(String id) {
        Vacina vacina = repository.findById(id).orElseThrow(
            () -> new RuntimeException("Vacina não encontrada!")
        );

        Lote lote = loteRepository.findByNumeroLote(vacina.getLote()).orElseThrow(
            () -> new RuntimeException("Lote não encontrado!")
        );

        List<Vacina> vacinas = repository.findAll();
        vacinas.remove(vacina);
        boolean flag = false;

        for (Vacina v : vacinas) {
            if (v.getId() != vacina.getId() && v.getFabricante().equals(vacina.getFabricante()) && v.getLote().equals(lote.getNumeroLote())) {
                flag = true;
                break;
            }
        }

        if (!flag){
            lote.getFabricante().remove(vacina.getFabricante());
        }

        flag = false;

        for (Vacina v : vacinas) {
            if (v.getLote().equals(vacina.getLote()) 
                && v.getId() != vacina.getId() && v.getNome().equals(vacina.getNome())){
                flag = true;
                break;
            }
        }

        if (!flag){
            lote.getVacinasAssociadas().remove(vacina.getNome());
        }

        if (lote.getFabricante().isEmpty() && lote.getVacinasAssociadas().isEmpty()) {
            loteRepository.deleteById(lote.getId());
        } else {
            loteRepository.save(lote);
        }

        repository.deleteById(id);
    }

    public List<VacinaResponseDTO> buscarVacinas() {
        return mapper.toDTOList(repository.findAll());
    }

    public List<VacinaResponseDTO> buscarPorLocal(String local){
        return mapper.toDTOList(repository.findByLocal(local));
    }
    public List<VacinaResponseDTO> buscarPorLote(String lote){
        return mapper.toDTOList(repository.findByLote(lote));
    }
    public List<VacinaResponseDTO> buscarPorNome(String nome){
        return mapper.toDTOList(repository.findByNome(nome));
    }

}
