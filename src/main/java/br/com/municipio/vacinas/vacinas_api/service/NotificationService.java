package br.com.municipio.vacinas.vacinas_api.service;

import br.com.municipio.vacinas.vacinas_api.model.DeviceToken;
import br.com.municipio.vacinas.vacinas_api.model.Notification;
import br.com.municipio.vacinas.vacinas_api.repository.DeviceTokenRepository;
import br.com.municipio.vacinas.vacinas_api.repository.NotificationRepository;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

        private final NotificationRepository notificationRepository;
        private final DeviceTokenRepository tokenRepository;

        public void notifyUser(String userId, String title, String body) throws Exception {

                // 1. Salva o histórico da notificação no banco de dados interno
                Notification notification = new Notification();
                notification.setUserId(userId);
                notification.setTitle(title);
                notification.setMessage(body);
                notification.setRead(false);
                notification.setCreatedAt(LocalDateTime.now());
                notificationRepository.save(notification);

                // 2. Busca todos os tokens de celulares registrados para este usuário
                List<DeviceToken> tokens = tokenRepository.findByUserId(userId);

                // 3. Configura a carga visual comum (Título e corpo da barra)
                com.google.firebase.messaging.Notification firebaseNotification = com.google.firebase.messaging.Notification
                                .builder()
                                .setTitle(title)
                                .setBody(body)
                                .build();

                // 4. Configuração de ALTISSIMA prioridade para o Android subir o pop-up
                // (banner)
                AndroidConfig androidConfig = AndroidConfig.builder()
                                .setPriority(AndroidConfig.Priority.HIGH)
                                .setNotification(AndroidNotification.builder()
                                                .setChannelId("high_importance_channel") // Obrigatório ser igual ao ID
                                                                                         // configurado no Flutter
                                                .setSound("default")
                                                .setClickAction("FLUTTER_NOTIFICATION_CLICK") // Diz ao Android para
                                                                                              // abrir o app ao clicar
                                                .build())
                                .build();

                // 5. Configuração de prioridade para o iOS (APNs) também acordar a tela e
                // emitir som
                ApnsConfig apnsConfig = ApnsConfig.builder()
                                .setAps(Aps.builder()
                                                .setSound("default")
                                                .setContentAvailable(true)
                                                .build())
                                .build();

                // 6. Dispara o push notification para cada dispositivo que o usuário possui
                // conectado
                for (DeviceToken token : tokens) {
                        try {
                                Message message = Message.builder()
                                                .setToken(token.getToken())
                                                .setNotification(firebaseNotification)
                                                .setAndroidConfig(androidConfig)
                                                .setApnsConfig(apnsConfig)
                                                // Opcional: .putData("tipo", "campanha") -> se quiser enviar dados
                                                // invisíveis ao Flutter
                                                .build();

                                // Envia diretamente através do Firebase Admin SDK nativo
                                FirebaseMessaging.getInstance().send(message);

                        } catch (Exception e) {
                                // Se um token específico falhar (Ex: usuário desinstalou o app), registra o
                                // erro e continua para os outros dispositivos
                                System.err.println("Falha ao enviar FCM para o token: " + token.getToken() + " | Erro: "
                                                + e.getMessage());
                        }
                }
        }

        public List<Notification> getUserNotifications(String userId) {
                return notificationRepository.findByUserId(userId);
        }
}
