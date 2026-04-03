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

    @Mapping(target = "rua", ignore = true)
    @Mapping(target = "numero", ignore = true)
    @Mapping(target = "bairro", ignore = true)
    @Mapping(target = "cep", ignore = true)
    @Mapping(target = "cidade", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "id", ignore = true)
    LocalResponseDTO toDTO(LocalVacina entity);
    
    @Mapping(target = "rua", ignore = true)
    @Mapping(target = "bairro", ignore = true)
    @Mapping(target = "numero", ignore = true)
    @Mapping(target = "cep", ignore = true)
    @Mapping(target = "cidade", ignore = true)
    @Mapping(target = "estado", ignore = true)
    List<LocalResponseDTO> toDTOList(List<LocalVacina> entities);



}
