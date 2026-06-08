package br.com.municipio.vacinas.vacinas_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import br.com.municipio.vacinas.vacinas_api.repository.NotificationRepository;
import br.com.municipio.vacinas.vacinas_api.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/count")
    public long count(Authentication authentication) {

        return notificationRepository
                .countByUserIdAndReadFalse(
                        usuarioRepository
                                .findUserByEmail(authentication.getName())
                                .orElseThrow()
                                .getId()
                );
    }
}
