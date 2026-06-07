package br.com.municipio.vacinas.vacinas_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import br.com.municipio.vacinas.vacinas_api.repository.NotificationRepository;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/count")
    public long count(Authentication authentication) {

        return notificationRepository
                .countByUserIdAndReadFalse(
                        authentication.getName()
                );
    }
}
