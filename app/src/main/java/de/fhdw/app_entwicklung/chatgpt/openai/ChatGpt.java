package de.fhdw.app_entwicklung.chatgpt.openai;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;

public class ChatGpt {

    private final String apiToken;

    public ChatGpt(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getChatCompletion(String query) {
        OpenAiService service = new OpenAiService(apiToken, Duration.ofSeconds(90));

        try {
            ChatMessage message = new ChatMessage(ChatMessageRole.USER.value(), query);
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                    .builder()
                    .model("gpt-3.5-turbo")
                    .messages(Collections.singletonList(message))
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
}