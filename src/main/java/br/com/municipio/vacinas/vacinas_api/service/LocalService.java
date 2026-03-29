package br.com.municipio.vacinas.vacinas_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mongodb.DuplicateKeyException;

import br.com.municipio.vacinas.vacinas_api.mapper.EnderecoMapper;
import br.com.municipio.vacinas.vacinas_api.mapper.LocalMapper;
import br.com.municipio.vacinas.vacinas_api.model.Endereco;
import br.com.municipio.vacinas.vacinas_api.model.LocalVacina;
import br.com.municipio.vacinas.vacinas_api.repository.EnderecoRepository;
import br.com.municipio.vacinas.vacinas_api.repository.LocalRepository;
import lombok.RequiredArgsConstructor;

import br.com.municipio.vacinas.vacinas_api.dto.LocalRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.LocalResponseDTO;


@RequiredArgsConstructor
@Service
public class LocalService {

    private final LocalRepository repository;
    private final EnderecoRepository enderecoRepository;
    private final LocalMapper mapper;
    private final EnderecoMapper endMapper;

    public LocalResponseDTO cadastrarLocal(LocalRequestDTO request) {

        if(repository.existsByName(request.getName())){
            throw new RuntimeException("Já existe um local com esse nome!");
        }
        try {
            Endereco endereco = endMapper.toEnderecoEntity(request);
            LocalResponseDTO response = mapper.toDTO(mapper.toEntity(request));

            response = mapper.toDTO(repository.save(mapper.toLocalVacina(response)));
            endereco.setLocalId(response.getId());
            
            enderecoRepository.save(endereco);
            response.setEnderecoId(enderecoRepository.findByLocalId(response.getId()).orElseThrow(null).getId());
            response = mapper.toDTO(repository.save(mapper.toLocalVacina(response)));
            response = endMapper.toLocalDTO(endereco, response);
            return response;

        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Erro ao cadastrar local");
        }
    }

    public LocalResponseDTO editarLocal(String id, LocalRequestDTO request) {
        LocalVacina localEntity = repository.findById(id).orElseThrow(() -> new RuntimeException("Local não encontrado!"));
        
        Endereco enderecoEntity = enderecoRepository.findByLocalId(id).orElseThrow(() -> new RuntimeException("Endereço não encontrado!"));

        LocalVacina localAtualizado = LocalVacina.builder()
            .name(request.getName() != null ? request.getName() : localEntity.getName())
            .id(id)
            .horarioFuncionamento(request.getHorarioFuncionamento() != null ? request.getHorarioFuncionamento() : localEntity.getHorarioFuncionamento())
            .build();
        
        Endereco enderecoAtualizado = Endereco.builder()
            .id(enderecoEntity.getId())
            .localId(id)
            .rua(request.getRua() != null ? request.getRua() : enderecoEntity.getRua())
            .numero(request.getNumero() != null ? request.getNumero() : enderecoEntity.getNumero())
            .bairro(request.getBairro() != null ? request.getBairro() : enderecoEntity.getBairro())
            .cidade(request.getCidade() != null ? request.getCidade() : enderecoEntity.getCidade())
            .estado(request.getEstado() != null ? request.getEstado() : enderecoEntity.getEstado())
            .cep(request.getCep() != null ? request.getCep() : enderecoEntity.getCep())
            .build();
        
        enderecoRepository.save(enderecoAtualizado);
        localAtualizado.setEnderecoId(enderecoAtualizado.getId());
        return endMapper.toLocalDTO(enderecoAtualizado, mapper.toDTO(repository.save(localAtualizado)));
    }

    public void excluirLocalPorId(String id) {
        enderecoRepository.deleteById(repository.findById(id).orElseThrow(() -> new RuntimeException("Endereco não encontrado!")).getEnderecoId());
        repository.deleteById(id);
    }

    public void excluirLocalPorNome(String name) {
        enderecoRepository.deleteById(repository.findByName(name).orElseThrow(
            () -> new RuntimeException("Endereço não encontrado!")
        ).getEnderecoId());
        repository.deleteByName(name);
    }

    public LocalResponseDTO buscarLocalPorId(String id) {
        Endereco endereco = enderecoRepository.findByLocalId(id).orElseThrow(
            () -> new RuntimeException("Endereço não encontrado!")
        );
        LocalResponseDTO localResponse =  mapper.toDTO(repository.findById(id).orElseThrow(() -> new RuntimeException("Local não encontrado")));
        localResponse = endMapper.toLocalDTO(endereco, localResponse);
        return localResponse;
    }

    public LocalResponseDTO buscarLocalPorNome(String name) {
        return buscarLocalPorId(repository.findByName(name).orElseThrow(
            () -> new RuntimeException("Local não encontrado!")
        ).getId());
    }

    public List<LocalResponseDTO> listarLocais() {
        List<LocalVacina> locais = repository.findAll();
        List<LocalResponseDTO> locaisResponse = new ArrayList<>();
        for (LocalVacina local : locais) {
            locaisResponse.add(endMapper.toLocalDTO(
                enderecoRepository.findByLocalId(local.getId()).orElse(
                    enderecoRepository.findById(local.getEnderecoId()).orElseThrow(
                        () -> new RuntimeException("Endereço não encontrado!")
                    )
                ),
                mapper.toDTO(local)
            ));
        }
        return locaisResponse;
    }

}
