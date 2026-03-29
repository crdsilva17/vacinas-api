package br.com.municipio.vacinas.vacinas_api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.municipio.vacinas.vacinas_api.model.LocalVacina;
import br.com.municipio.vacinas.vacinas_api.dto.LocalRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.LocalResponseDTO;

@Mapper(componentModel = "spring")
public interface LocalMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enderecoId", ignore = true)
    LocalVacina toEntity(LocalRequestDTO dto);

    LocalVacina toLocalVacina(LocalResponseDTO dto);

    LocalResponseDTO toDTO(LocalVacina entity);

    List<LocalResponseDTO> toDTOList(List<LocalVacina> entities);

}
