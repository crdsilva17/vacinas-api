package br.com.municipio.vacinas.vacinas_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mongodb.DuplicateKeyException;

import br.com.municipio.vacinas.vacinas_api.repository.LocalRepository;
import br.com.municipio.vacinas.vacinas_api.repository.LoteRepository;
import br.com.municipio.vacinas.vacinas_api.repository.VacinaRepository;
import lombok.RequiredArgsConstructor;
import br.com.municipio.vacinas.vacinas_api.dto.VacinaRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.VacinaResponseDTO;
import br.com.municipio.vacinas.vacinas_api.mapper.VacinaMapper;
import br.com.municipio.vacinas.vacinas_api.model.LocalVacina;
import br.com.municipio.vacinas.vacinas_api.model.Lote;

@Service
@RequiredArgsConstructor
public class VacinaService {

    private final VacinaRepository repository;
    private final LocalRepository localRepository;
    private final LoteRepository loteRepository;
    private final VacinaMapper mapper;

    public VacinaResponseDTO cadastrarVacina(VacinaRequestDTO request) {

        if (repository.existsByNomeAndLoteAndLocal(request.getNome(), request.getLote(), request.getLocal())) {
            throw new RuntimeException("Já existe uma vacina com esse nome e lote!");
        }
        try {

            if (!localRepository.existsByName(request.getLocal())) {

                LocalVacina local = new LocalVacina();
                local.setName(request.getLocal());
                localRepository.save(local);
            }

            if (!loteRepository.existsByNumeroLote(request.getLote())) {
                
                Lote lote = new Lote();
                lote.setNumeroLote(request.getLote());
                lote.setFabricante(request.getFabricante());
                lote.setDataFabricacao(request.getDataFabricacao());
                lote.setDataValidade(request.getDataValidade());
                lote.setQuantidadeDisponivel(request.getQuantidadeDisponivel());
                lote.setTipo(request.getDescricao());
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
        repository.deleteById(id);
    }

    public void excluirVacinaPorNome(String nome) {
        List<VacinaResponseDTO> vacinas = buscarPorNome(nome);
        vacinas.forEach(vacina -> repository.deleteById(vacina.getId()));
    }

    public void excluirVacinaPorLote(String lote) {
        List<VacinaResponseDTO> vacinas = buscarPorLote(lote);
        vacinas.forEach(vacina -> repository.deleteById(vacina.getId()));
    }

    public void excluirVacinaPorNomeELote(String nome, String lote) {
        List<VacinaResponseDTO> vacinas = buscarPorNome(nome);
        vacinas.stream()
                .filter(vacina -> vacina.getLote().equals(lote))
                .forEach(vacina -> repository.deleteById(vacina.getId()));
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
