package br.com.municipio.vacinas.vacinas_api.service;

import org.springframework.stereotype.Service;

import com.mongodb.DuplicateKeyException;

import br.com.municipio.vacinas.vacinas_api.mapper.LocalMapper;
import br.com.municipio.vacinas.vacinas_api.repository.LocalRepository;
import lombok.RequiredArgsConstructor;

import br.com.municipio.vacinas.vacinas_api.dto.LocalRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.LocalResponseDTO;


@RequiredArgsConstructor
@Service
public class LocalService {

    private final LocalRepository repository;
    private final LocalMapper mapper;

    public LocalResponseDTO cadastrarLocal(LocalRequestDTO request) {

        if(repository.existsByName(request.getName())){
            throw new RuntimeException("Já existe um local com esse nome!");
        }
        try {

            return mapper.toDTO(repository.save(mapper.toEntity(request)));

        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Erro ao cadastrar local");
        }
    }

    public LocalResponseDTO editarVacina(LocalRequestDTO request) {
        return mapper.toDTO(repository.save(mapper.toEntity(request)));
    }

    public void excluirLocalPorId(String id) {
        repository.deleteById(id);
    }

    public void excluirLocalPorNome(String name) {
        repository.deleteByName(name);
    }

    public LocalResponseDTO buscarLocalPorId(String id) {
        return mapper.toDTO(repository.findById(id).orElseThrow(() -> new RuntimeException("Local não encontrado")));
    }

    public LocalResponseDTO buscarLocalPorNome(String name) {
        return mapper.toDTO(repository.findByName(name).orElseThrow(() -> new RuntimeException("Local não encontrado")));
    }

}
