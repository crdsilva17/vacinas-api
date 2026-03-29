package br.com.municipio.vacinas.vacinas_api.service;

import org.springframework.stereotype.Service;

import com.mongodb.DuplicateKeyException;

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

    public LocalResponseDTO cadastrarLocal(LocalRequestDTO request) {

        if(repository.existsByName(request.getName())){
            throw new RuntimeException("Já existe um local com esse nome!");
        }
        try {
            Endereco endereco = mapper.toEnderecoEntity(request);
            request.setEnderecoId(endereco.getId());
            LocalResponseDTO response = mapper.toDTO(repository.save(mapper.toEntity(request)));
            endereco.setLocalId(response.getId());
            enderecoRepository.save(endereco);
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
            .enderecoId(request.getEnderecoId() != null ? request.getEnderecoId() : enderecoEntity.getId())
            .build();
        
        Endereco enderecoAtualizado = Endereco.builder()
            .id(request.getEnderecoId() != null ? request.getEnderecoId() : enderecoEntity.getId())
            .localId(id)
            .rua(request.getRua() != null ? request.getRua() : enderecoEntity.getRua())
            .numero(request.getNumero() != null ? request.getNumero() : enderecoEntity.getNumero())
            .bairro(request.getBairro() != null ? request.getBairro() : enderecoEntity.getBairro())
            .cidade(request.getCidade() != null ? request.getCidade() : enderecoEntity.getCidade())
            .estado(request.getEstado() != null ? request.getEstado() : enderecoEntity.getEstado())
            .cep(request.getCep() != null ? request.getCep() : enderecoEntity.getCep())
            .build();
        
        enderecoRepository.save(enderecoAtualizado);
        return mapper.toDTO(repository.save(localAtualizado));
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
