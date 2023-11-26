package de.fhdw.app_entwicklung.chatgpt.openai;

import androidx.annotation.NonNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import de.fhdw.app_entwicklung.chatgpt.model.Author;
import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.Message;

public class MockChatGpt implements IChatGpt {

    public MockChatGpt(String apiToken) {
    }

    @Override
    public CompletableFuture<Message> getChatCompletion(@NonNull Chat chat) {
        final Message msg = new Message(Author.Assistant, MESSAGES[ThreadLocalRandom.current().nextInt(0, MESSAGES.length)]);
        chat.addMessage(msg);
        return CompletableFuture.completedFuture(msg);
    }

    public static final String[] MESSAGES = {
            "Das Wetter bleibt weiterhin trüb.",
            "Heute wird es Sonnig mit 20°C im Schatten der prefekte Tag um an den See zu gehen.",
            "Das Wetter wird stürmisch mit 5°C."
    };
}