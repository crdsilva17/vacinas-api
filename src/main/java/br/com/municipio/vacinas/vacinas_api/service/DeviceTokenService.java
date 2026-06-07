package br.com.municipio.vacinas.vacinas_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import br.com.municipio.vacinas.vacinas_api.model.DeviceToken;
import br.com.municipio.vacinas.vacinas_api.repository.DeviceTokenRepository;

@Service
@RequiredArgsConstructor
public class DeviceTokenService {

    private final DeviceTokenRepository repository;

    public void saveToken(
            String userId,
            String token) {

        repository.findByToken(token)
                .ifPresentOrElse(
                        existing -> {
                            existing.setUserId(userId);
                            repository.save(existing);
                        },
                        () -> {
                            DeviceToken deviceToken = new DeviceToken();

                            deviceToken.setUserId(userId);
                            deviceToken.setToken(token);

                            repository.save(deviceToken);
                        });
    }
}