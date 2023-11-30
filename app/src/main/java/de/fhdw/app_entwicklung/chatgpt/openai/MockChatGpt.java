package de.fhdw.app_entwicklung.chatgpt.openai;

import androidx.annotation.NonNull;

import de.fhdw.app_entwicklung.chatgpt.model.Chat;

public class MockChatGpt implements IChatGpt {

    @Override
    public String getChatCompletion(@NonNull Chat chat) {

        return "test";
    }
}
