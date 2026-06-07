package br.com.municipio.vacinas.vacinas_api.security;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {

        try {
            /*
             * InputStream serviceAccount =
             * new ClassPathResource(
             * "firebase-service-account.json")
             * .getInputStream();
             */
            String firebaseCredentials =
                    System.getenv("FIREBASE_CREDENTIALS");
            
            ByteArrayInputStream serviceAccount =
                    new ByteArrayInputStream(
                            firebaseCredentials.getBytes(
                                    StandardCharsets.UTF_8));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(
                            GoogleCredentials
                                    .fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}