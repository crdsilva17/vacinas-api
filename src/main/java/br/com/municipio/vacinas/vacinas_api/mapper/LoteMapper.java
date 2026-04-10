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
    @Mapping(target = "fabricante", source = "fabricante")
    @Mapping(target = "vacinasAssociadas", ignore = true)
    Lote toEntity(LoteRequestDTO dto);

    @Mapping(target = "fabricante", source = "fabricante")
    LoteResponseDTO toDTO(Lote lote);

    List<LoteResponseDTO> toDTOList(List<Lote> entities);

    default List<String> mapFabricante(String fabricante) {
        return fabricante != null ? java.util.Arrays.asList(fabricante) : null;
    }

    default String mapFabricante(List<String> fabricante) {
        return (fabricante != null && !fabricante.isEmpty()) ? fabricante.get(0) : null;
    }

}
