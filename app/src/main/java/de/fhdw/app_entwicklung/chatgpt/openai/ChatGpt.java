package de.fhdw.app_entwicklung.chatgpt.openai;

import android.util.Log;

import androidx.annotation.NonNull;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import de.fhdw.app_entwicklung.chatgpt.MainActivity;
import de.fhdw.app_entwicklung.chatgpt.model.Author;
import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.Message;

public class ChatGpt implements IChatGpt {
    private final String apiToken;

    public ChatGpt(String apiToken) {
        this.apiToken = apiToken;
    }

    @Override
    public CompletableFuture<Message> getChatCompletion(@NonNull Chat chat) {
        return CompletableFuture.supplyAsync(() -> {
            final OpenAiService service = new OpenAiService(apiToken, Duration.ofSeconds(90));
            try {
                final List<ChatMessage> messages = chat.getMessages().stream()
                        .map(this::toChatMessage)
                        .collect(Collectors.toList());

                final ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                        .model("gpt-3.5-turbo")
                        .messages(messages)
                        .n(1)
                        .maxTokens(2048)
                        .logitBias(new HashMap<>())
                        .build();

                final ChatCompletionResult result = service.createChatCompletion(chatCompletionRequest);
                if (result.getChoices().size() != 1) {
                    throw new RuntimeException("Received unexpected number of chat completions: should be 1, but received " + result.getChoices().size());
                }
                final Message msg = new Message(Author.Assistant, result.getChoices().get(0).getMessage().getContent());
                chat.addMessage(msg);
                return msg;
            } finally {
                service.shutdownExecutor();
            }
        }, MainActivity.backgroundExecutorService).exceptionally(throwable -> {
            Log.e(ChatGpt.class.getSimpleName(), "Error requesting answer from ChatGPT", throwable);
            return new Message(Author.System, throwable.getLocalizedMessage());
        });
    }

    @NonNull
    private ChatMessage toChatMessage(@NonNull Message message) {
        return new ChatMessage(toRole(message.author).value(), message.message);
    }

    private ChatMessageRole toRole(Author author) {
        switch (author) {
            case User:
                return ChatMessageRole.USER;
            case Assistant:
                return ChatMessageRole.ASSISTANT;
            case System:
                return ChatMessageRole.SYSTEM;
            default:
                throw new RuntimeException("Unknown author " + author);
        }
    }
}