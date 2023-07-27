package de.fhdw.app_entwicklung.chatgpt.openai;

import androidx.annotation.NonNull;

import de.fhdw.app_entwicklung.chatgpt.model.Chat;

public interface IChatGpt {
    String getChatCompletion(@NonNull Chat chat);
}
