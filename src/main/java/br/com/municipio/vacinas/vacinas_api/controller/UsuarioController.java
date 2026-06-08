package br.com.municipio.vacinas.vacinas_api.controller;

import br.com.municipio.vacinas.vacinas_api.dto.UserRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.UsuarioResponseDTO;
import br.com.municipio.vacinas.vacinas_api.mapper.UsuarioMapper;
import br.com.municipio.vacinas.vacinas_api.model.Usuario;
import br.com.municipio.vacinas.vacinas_api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioMapper mapper;
    private final UsuarioService service;

    @GetMapping
    public ResponseEntity<UsuarioResponseDTO> buscarUsuario(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(mapper.toDTO(usuario));
    }

    @PutMapping
    public ResponseEntity<UsuarioResponseDTO> updateUser(UserRequestDTO request) {
        System.out.println("Atualizando usuário: " + request);
        System.out.println(request.email());
        return ResponseEntity.ok(service.updateUserByEmail(request));

    }
}
