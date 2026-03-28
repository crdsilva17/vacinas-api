package br.com.municipio.vacinas.vacinas_api.mapper;

import br.com.municipio.vacinas.vacinas_api.dto.VacinaResponseDTO;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.municipio.vacinas.vacinas_api.dto.VacinaRequestDTO;
import br.com.municipio.vacinas.vacinas_api.model.Vacina;

@Mapper(componentModel = "spring")
public interface VacinaMapper {

    @Mapping(target = "id", ignore = true)
    Vacina toEntity(VacinaRequestDTO dto);

    VacinaResponseDTO toDTO(Vacina vacina);

    List<VacinaResponseDTO> toDTOList(List<Vacina> vacinas);

}
