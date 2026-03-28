package br.com.municipio.vacinas.vacinas_api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.municipio.vacinas.vacinas_api.model.Lote;
import br.com.municipio.vacinas.vacinas_api.dto.LoteRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.LoteResponseDTO;

@Mapper(componentModel = "spring")
public interface LoteMapper {

    @Mapping(target = "id", ignore = true)
    Lote toEntity(LoteRequestDTO dto);

    LoteResponseDTO toDTO(Lote entity);

    List<LoteResponseDTO> toDTOList(List<Lote> entities);

}
