package br.com.municipio.vacinas.vacinas_api.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.municipio.vacinas.vacinas_api.dto.DeviceTokenRequest;
import br.com.municipio.vacinas.vacinas_api.model.DeviceToken;
import br.com.municipio.vacinas.vacinas_api.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/device-token")
@RequiredArgsConstructor
public class DeviceTokenController {

    private final DeviceTokenRepository repository;

    @PostMapping
    public void saveToken(
            @RequestBody DeviceTokenRequest request,
            Authentication authentication){

        DeviceToken token =
                new DeviceToken();

        token.setUserId(
                authentication.getName());

        token.setToken(
                request.token());

        repository.save(token);
    }
}
