package br.com.municipio.vacinas.vacinas_api.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mongodb.DuplicateKeyException;

import br.com.municipio.vacinas.vacinas_api.dto.LoteRequestDTO;
import br.com.municipio.vacinas.vacinas_api.mapper.LoteMapper;
import br.com.municipio.vacinas.vacinas_api.repository.LoteRepository;
import br.com.municipio.vacinas.vacinas_api.dto.LoteResponseDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LoteService {

    private final LoteRepository repository;
    private final LoteMapper mapper;

    public LoteResponseDTO cadastrarLote(LoteRequestDTO request) {
        if (repository.existsByNumeroLote(request.getNumeroLote())){
            throw new RuntimeException("Já existe um lote com esse número!");
        }
        try {

            return mapper.toDTO(repository.save(mapper.toEntity(request)));

        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Erro ao cadastrar lote");
        }
    }

    public LoteResponseDTO editarLote(LoteRequestDTO request) {
        return mapper.toDTO(repository.save(mapper.toEntity(request)));
    }

    public void excluirLotePorId(String id) {
        repository.deleteById(id);
    }

    public void excluirLotePorNumero(String numeroLote) {
        repository.deleteByNumeroLote(numeroLote);
    }

    public List<LoteResponseDTO> buscarPorNumero(String numeroLote) {
        return mapper.toDTOList(Arrays.asList(repository.findByNumeroLote(numeroLote).orElseThrow(() -> new RuntimeException("Lote não encontrado!"))));
    }

}
