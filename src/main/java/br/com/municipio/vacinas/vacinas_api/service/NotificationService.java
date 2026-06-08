package br.com.municipio.vacinas.vacinas_api.service;

import br.com.municipio.vacinas.vacinas_api.model.DeviceToken;
import br.com.municipio.vacinas.vacinas_api.model.Notification;
import br.com.municipio.vacinas.vacinas_api.repository.DeviceTokenRepository;
import br.com.municipio.vacinas.vacinas_api.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

        private final NotificationRepository notificationRepository;

        private final DeviceTokenRepository tokenRepository;

        private final FirebaseService firebaseService;

        public void notifyUser(
                        String userId,
                        String title,
                        String body)
                        throws Exception {

                Notification notification = new Notification();
                notification.setUserId(userId);
                notification.setTitle(title);
                notification.setMessage(body);
                notification.setRead(false);
                notification.setCreatedAt(
                                LocalDateTime.now());

                notificationRepository.save(
                                notification);

                List<DeviceToken> tokens = tokenRepository.findByUserId(
                                userId);

                for (DeviceToken token : tokens) {
                        firebaseService.sendNotification(
                                        token.getToken(),
                                        title,
                                        body);
                }
        }

        public List<Notification> getUserNotifications(
                        String userId) {

                return notificationRepository.findByUserId(
                                userId);
        }
}
