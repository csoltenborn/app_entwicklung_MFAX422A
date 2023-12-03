package de.fhdw.app_entwicklung.chatgpt.openai;

import androidx.annotation.NonNull;

import java.util.concurrent.ThreadLocalRandom;

import de.fhdw.app_entwicklung.chatgpt.model.Chat;

public class MockChatGpt implements IChatGpt {

    /** @noinspection unused*/
    public MockChatGpt(String apiToken, String model) {
        // ignore...
    }

    @Override
    public String getChatCompletion(@NonNull Chat chat) {
        return MESSAGES[ThreadLocalRandom.current().nextInt(0, MESSAGES.length)];
    }

    public static final String[] MESSAGES = {
        "Let me tell you something, folks. John F. Kennedy, he was a guy. He was a guy, he was a president, and they say he was a pretty good president. People liked him, they really did. But you know what? I think I could have been a much better president than JFK, I really do. I mean, look at what I've done, the things I've accomplished. Nobody's accomplished more than me. So yeah, JFK, he was okay, but I don't think he can hold a candle to me. Not even close.",
        "Look, I know a lot of great things, okay? And when it comes to JFK, I know he was born in Massachusetts. He was a Democrat, and let me tell you, I've dealt with a lot of Democrats, folks. They're not always the greatest, believe me. But JFK, he had some charm, some charisma. People were drawn to him. And apparently, he had this thing called the New Frontier, where he wanted to promote social welfare programs and space exploration. But you know what? I'm all about the America First agenda. I'm focused on jobs, the economy, and making America great again. So while JFK may have had his own ideas, I think my vision for the country is much better, folks. Just saying.",
        "Let me tell you, I know a lot of things, okay? I've got a tremendous memory, the best memory there is. When it comes to JFK, he was the 35th president of the United States, and he served from 1961 until 1963. Now, during his presidency, there were some major events. There was the Cuban Missile Crisis, where he stood up to the Soviets and made it clear that America wouldn't back down. And let me tell you, that was a big deal. People thought we were on the brink of World War III, but JFK, he handled it like a boss. Now, I have to mention something else, folks. Unfortunately, JFK's presidency was cut short. He was assassinated in Dallas, Texas in November 1963. It's a tragic event, no doubt about it. And I know there are a lot of conspiracy theories surrounding his assassination, but hey, I'm not here to delve into that. I'm here to talk about what I know, and what I know is that JFK's presidency, for the short time it lasted, left an impact on this country. Whether you agree with his policies or not, he was a figure that people remember. And that's all I have to say about that."
    };
}