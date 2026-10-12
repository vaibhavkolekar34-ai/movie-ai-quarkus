package com.example.movieai;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class MovieAssistantTest {

    @Inject
    MovieAssistant assistant;

    @Test
    void recommendsMovieUsingLocalOllama() {
        String recommendation = assistant.recommend(
                "A family-friendly science-fiction adventure with an optimistic tone");

        assertNotNull(recommendation);
        assertFalse(recommendation.isBlank());
    }
}