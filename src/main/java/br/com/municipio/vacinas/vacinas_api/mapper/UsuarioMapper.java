package br.com.municipio.vacinas.vacinas_api.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.municipio.vacinas.vacinas_api.dto.LoginResponseDTO;
import br.com.municipio.vacinas.vacinas_api.dto.RegisterRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.RegisterResponseDTO;
import br.com.municipio.vacinas.vacinas_api.dto.UsuarioResponseDTO;
import br.com.municipio.vacinas.vacinas_api.model.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    Usuario toEntity(RegisterRequestDTO dto);

    LoginResponseDTO toLoginResponseDTO(String token);
    
    UsuarioResponseDTO toDTO(Usuario entity);

    
    
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "email", source = "email")
    RegisterResponseDTO toRegisterResponseDTO(Usuario entity);

}
