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

    /* 
        Método para cadastrar uma nova Unidade Básica de Saúde
        Recebe um objeto LocalRequestDTO contendo as informações da unidade a ser cadastrada
        Verifica se já existe uma unidade com o mesmo nome, caso exista lança uma exceção
        Caso não exista, cria um novo objeto LocalVacina e Endereco, salva no banco de dados e retorna um objeto LocalResponseDTO com as informações da unidade cadastrada 
    */
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

    /*
        Método para editar uma Unidade Básica de Saúde existente
        Recebe o ID da unidade a ser editada e um objeto LocalRequestDTO contendo as informações atualizadas
        Busca a unidade no banco de dados pelo ID, caso não encontre lança uma exceção
        Atualiza as informações da unidade e do endereço, salva no banco de dados e retorna um objeto LocalResponseDTO com as informações atualizadas 
    */
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

    /*
        Método para excluir uma Unidade Básica de Saúde existente
        Recebe o ID da unidade a ser excluída
        Busca a unidade no banco de dados pelo ID, caso não encontre lança uma exceção
        Exclui o endereço associado à unidade e a própria unidade do banco de dados 
    */
    public void excluirLocalPorId(String id) {
        enderecoRepository.deleteById(repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Endereco não encontrado!"))
        .getEnderecoId());
        repository.deleteById(id);
    }

    /*
        Método para excluir uma Unidade Básica de Saúde existente
        Recebe o nome da unidade a ser excluída
        Busca a unidade no banco de dados pelo nome, caso não encontre lança uma exceção
        Exclui o endereço associado à unidade e a própria unidade do banco de dados 
    */
    public void excluirLocalPorNome(String name) {
        enderecoRepository.deleteById(repository.findByName(name).orElseThrow(
            () -> new RuntimeException("Endereço não encontrado!")
        ).getEnderecoId());
        repository.deleteByName(name);
    }

    /*
        Método para buscar uma Unidade Básica de Saúde existente
        Recebe o ID da unidade a ser buscada
        Busca a unidade no banco de dados pelo ID, caso não encontre lança uma exceção
        Retorna um objeto LocalResponseDTO com as informações da unidade encontrada
    */
    public LocalResponseDTO buscarLocalPorId(String id) {
        Endereco endereco = enderecoRepository.findByLocalId(id).orElseThrow(
            () -> new RuntimeException("Endereço não encontrado!")
        );
        LocalResponseDTO localResponse =  mapper.toDTO(repository.findById(id).orElseThrow(() -> new RuntimeException("Local não encontrado")));
        localResponse = endMapper.toLocalDTO(endereco, localResponse);
        return localResponse;
    }

    /*
        Método para buscar uma Unidade Básica de Saúde existente
        Recebe o nome da unidade a ser buscada
        Busca a unidade no banco de dados pelo nome, caso não encontre lança uma exceção
        Retorna um objeto LocalResponseDTO com as informações da unidade encontrada
    */
    public LocalResponseDTO buscarLocalPorNome(String name) {
        return buscarLocalPorId(repository.findByName(name).orElseThrow(
            () -> new RuntimeException("Local não encontrado!")
        ).getId());
    }

    /*
        Método para listar todas as Unidades Básicas de Saúde
        Retorna uma lista de objetos LocalResponseDTO com as informações das unidades encontradas
    */
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
