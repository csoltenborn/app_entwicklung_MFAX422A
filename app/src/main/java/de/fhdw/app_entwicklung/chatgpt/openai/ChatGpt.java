package de.fhdw.app_entwicklung.chatgpt.openai;

import androidx.annotation.NonNull;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import de.fhdw.app_entwicklung.chatgpt.model.Author;
import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.Message;

public class ChatGpt implements IChatGpt {

    private final String apiToken;
    private final String model;

    public ChatGpt(String apiToken, String model) {
        this.apiToken = apiToken;
        this.model = model;
    }

    @Override
    public String getChatCompletion(@NonNull Chat chat) {
        OpenAiService service = new OpenAiService(apiToken, Duration.ofSeconds(90));

        try {
            List<ChatMessage> messages = chat.getMessages().stream()
                    .map(this::toChatMessage)
                    .collect(Collectors.toList());
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(messages)
                    .n(1)
                    .maxTokens(2048)
                    .logitBias(new HashMap<>())
                    .build();

            ChatCompletionResult result = service.createChatCompletion(chatCompletionRequest);
            if (result.getChoices().size() != 1) {
                throw new RuntimeException("Received unexpected number of chat completions: should be 1, but received " + result.getChoices().size());
            }

            return result.getChoices().get(0).getMessage().getContent();
        }
        finally {
            service.shutdownExecutor();
        }
    }

    @NonNull
    private ChatMessage toChatMessage(@NonNull Message message) {
        return new ChatMessage(toRole(message.author).value(), message.message);
    }

    private ChatMessageRole toRole(Author author) {
        switch (author) {
            case User: return ChatMessageRole.USER;
            case Assistant: return ChatMessageRole.ASSISTANT;
            case System: return ChatMessageRole.SYSTEM;
            default: throw new RuntimeException("Unknown author " + author);
        }
    }
}