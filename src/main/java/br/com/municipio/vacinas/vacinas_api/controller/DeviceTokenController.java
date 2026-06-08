package br.com.municipio.vacinas.vacinas_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.municipio.vacinas.vacinas_api.dto.DeviceTokenRequest;
import br.com.municipio.vacinas.vacinas_api.exception.UserUpdateException;
import br.com.municipio.vacinas.vacinas_api.repository.UsuarioRepository;
import br.com.municipio.vacinas.vacinas_api.service.DeviceTokenService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/device-token")
@RequiredArgsConstructor
public class DeviceTokenController {

        private final DeviceTokenService service;
        private final UsuarioRepository userRepository;

        @PostMapping
        public ResponseEntity<Void> registerToken(
                        @RequestBody DeviceTokenRequest request,
                        Authentication authentication) {

                String userId = userRepository.findUserByEmail(authentication.getName())
                                .orElseThrow(() -> new UserUpdateException("Usuário não encontrado!"))
                                .getId();

                service.saveToken(
                                userId,
                                request.token());

                return ResponseEntity.ok().build();
        }
}
