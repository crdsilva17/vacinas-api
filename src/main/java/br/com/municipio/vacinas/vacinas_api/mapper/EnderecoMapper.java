package br.com.municipio.vacinas.vacinas_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.municipio.vacinas.vacinas_api.dto.LocalRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.LocalResponseDTO;
import br.com.municipio.vacinas.vacinas_api.model.Endereco;


@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "localId", ignore = true)
    Endereco toEnderecoEntity(LocalRequestDTO dto);

    @Mapping(target = "name", source = "local.name")
    @Mapping(target = "enderecoId", source = "endereco.id")
    @Mapping(target = "horarioFuncionamento", source = "local.horarioFuncionamento")
    @Mapping(target = "id", source = "local.id")
    @Mapping(target = "rua", source = "endereco.rua")
    @Mapping(target = "numero", source = "endereco.numero")
    @Mapping(target = "bairro", source = "endereco.bairro")
    @Mapping(target = "cidade", source = "endereco.cidade")
    @Mapping(target = "estado", source = "endereco.estado")
    @Mapping(target = "cep", source = "endereco.cep")
    LocalResponseDTO toLocalDTO(Endereco endereco, LocalResponseDTO local);

}
