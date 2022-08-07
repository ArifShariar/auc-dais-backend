package com.morse_coders.aucdaisbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AucDaisBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AucDaisBackendApplication.class, args);
    }
}
