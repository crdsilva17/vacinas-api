package br.com.municipio.vacinas.vacinas_api.service;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service
public class FirebaseService {

    public void sendNotification(
            String token,
            String title,
            String body)
            throws FirebaseMessagingException {

        Message message =
                Message.builder()
                        .setToken(token)
                        .setNotification(
                                Notification.builder()
                                        .setTitle(title)
                                        .setBody(body)
                                        .build())
                        .build();

        FirebaseMessaging
                .getInstance()
                .send(message);
    }
}
