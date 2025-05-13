package tani.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {


    @Bean
    public FirebaseApp intializeFirebase() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(
                "src/main/resources/tanistorage-6ffaa-firebase-adminsdk-fbsvc-8277b7287c.json"
        );


        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("tanistorage-6ffaa.firebasestorage.app")
                .build();


        if(FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        }


        return null;
    }


}