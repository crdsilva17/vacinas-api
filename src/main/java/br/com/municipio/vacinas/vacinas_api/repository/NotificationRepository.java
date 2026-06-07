package br.com.municipio.vacinas.vacinas_api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.municipio.vacinas.vacinas_api.model.Notification;

@Repository
public interface NotificationRepository
        extends MongoRepository<Notification, String> {

    long countByUserIdAndReadFalse(
            String userId);

    List<Notification> findByUserId(String userId);

}
