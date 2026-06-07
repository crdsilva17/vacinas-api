package br.com.municipio.vacinas.vacinas_api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.municipio.vacinas.vacinas_api.model.DeviceToken;

@Repository
public interface DeviceTokenRepository
        extends MongoRepository<DeviceToken,String>{

    List<DeviceToken> findByUserId(String userId);
}
