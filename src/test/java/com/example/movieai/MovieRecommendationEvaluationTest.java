package com.example.movieai;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import io.quarkiverse.langchain4j.testing.evaluation.Parameters;
import io.quarkiverse.langchain4j.evaluation.junit5.Evaluate;
import io.quarkiverse.langchain4j.testing.evaluation.EvaluationReport;
import io.quarkiverse.langchain4j.testing.evaluation.EvaluationResult;
import io.quarkiverse.langchain4j.testing.evaluation.EvaluationSample;
import io.quarkiverse.langchain4j.testing.evaluation.EvaluationStrategy;
import io.quarkiverse.langchain4j.testing.evaluation.Samples;
import io.quarkiverse.langchain4j.testing.evaluation.Scorer;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@QuarkusTest
@Evaluate
class MovieRecommendationEvaluationTest {

    @Inject
    RecommendationFunction recommendationFunction;

    @Test
    void recommendationsMeetBasicQualityRequirements(Scorer scorer) {
        Samples<String> samples = new Samples<>(
                sample("family-science-fiction",
                        "A family-friendly science-fiction adventure with an optimistic tone"),
                sample("animated-comedy",
                        "A light animated comedy suitable for children and adults"));

        EvaluationReport<String> report = scorer.evaluate(
                samples,
            recommendationFunction,
                new RecommendationQualityStrategy());

        assertEquals(100.0, report.score());
    }

    private static EvaluationSample<String> sample(String name, String preference) {
        return EvaluationSample.<String>builder()
                .withName(name)
                .withParameter(preference)
                .withExpectedOutput("A concise movie recommendation with a reason")
                .build();
    }

    private static final class RecommendationQualityStrategy implements EvaluationStrategy<String> {

        @Override
        public EvaluationResult evaluate(EvaluationSample<String> sample, String output) {
            if (output == null || output.isBlank()) {
                return EvaluationResult.failed("The model returned an empty recommendation");
            }
            if (output.length() > 600) {
                return EvaluationResult.failed("The recommendation was not concise");
            }
            if (output.toLowerCase().contains("i cannot")) {
                return EvaluationResult.failed("The model did not provide a recommendation");
            }
            return EvaluationResult.passed(1.0)
                    .withExplanation("The model returned a concise recommendation");
        }
    }

    @Singleton
    static class RecommendationFunction implements Function<Parameters, String> {

        @Inject
        MovieAssistant assistant;

        @Override
        @ActivateRequestContext
        public String apply(Parameters parameters) {
            return assistant.recommend(parameters.get(0));
        }
    }
}