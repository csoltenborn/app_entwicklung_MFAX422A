package de.fhdw.app_entwicklung.chatgpt.openai;

import androidx.annotation.NonNull;

import java.util.concurrent.CompletableFuture;

import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.Message;

public interface IChatGpt {
    CompletableFuture<Message> getChatCompletion(@NonNull Chat chat);
}
