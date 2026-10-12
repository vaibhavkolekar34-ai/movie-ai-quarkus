package com.example.movieai;

import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/movies/recommend")
@Produces(MediaType.APPLICATION_JSON)
public class MovieRecommendationResource {

    @Inject
    MovieAssistant assistant;

    @GET
    public RecommendationResponse recommend(
            @QueryParam("preferences")
            @DefaultValue("A family-friendly science-fiction adventure with an optimistic tone")
            String preferences) {
        return new RecommendationResponse(preferences, assistant.recommend(preferences));
    }

    public record RecommendationResponse(String preferences, String recommendation) {
    }
}