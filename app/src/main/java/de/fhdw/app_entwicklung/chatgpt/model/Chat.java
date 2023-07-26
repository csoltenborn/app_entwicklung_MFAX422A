package de.fhdw.app_entwicklung.chatgpt.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chat {

    private final List<Message> messages = new ArrayList<>();

    public Chat() {
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }
}