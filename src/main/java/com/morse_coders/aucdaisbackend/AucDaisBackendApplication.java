package com.morse_coders.aucdaisbackend;

import com.morse_coders.aucdaisbackend.FileStorage.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({
            FileStorageProperties.class
})
public class AucDaisBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AucDaisBackendApplication.class, args);
    }
}
