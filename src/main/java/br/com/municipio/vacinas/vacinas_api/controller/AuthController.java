package br.com.municipio.vacinas.vacinas_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.municipio.vacinas.vacinas_api.dto.LoginRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.LoginResponseDTO;
import br.com.municipio.vacinas.vacinas_api.dto.RegisterRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.RegisterResponseDTO;
import br.com.municipio.vacinas.vacinas_api.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UsuarioService service;

    public AuthController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(service.login(request));
    }

}
