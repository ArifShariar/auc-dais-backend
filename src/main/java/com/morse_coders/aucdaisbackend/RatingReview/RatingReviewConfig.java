package com.morse_coders.aucdaisbackend.RatingReview;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RatingReviewConfig {
    @Bean
    CommandLineRunner init(RatingReviewRepository ratingReviewRepository) {
        return args -> {
        };
    }
}
