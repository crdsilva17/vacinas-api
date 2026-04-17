package br.com.municipio.vacinas.vacinas_api.mapper;

import br.com.municipio.vacinas.vacinas_api.dto.CampanhaRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.CampanhaResponseDTO;
import br.com.municipio.vacinas.vacinas_api.model.CampanhaVacinacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CampanhaMapper {
    @Mapping(target = "id", ignore = true)
    CampanhaVacinacao toCampanhaVacinacao(CampanhaRequestDTO dto);

    CampanhaResponseDTO toDTO(CampanhaVacinacao entity);
}
