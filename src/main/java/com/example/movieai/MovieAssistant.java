package com.example.movieai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface MovieAssistant {

    @SystemMessage("""
            You are a movie recommendation assistant.
            Recommend one family-friendly movie that matches the user's preferences.
            Respond in one or two sentences, explain why it matches, and avoid spoilers.
            """)
    String recommend(@UserMessage String preferences);
}