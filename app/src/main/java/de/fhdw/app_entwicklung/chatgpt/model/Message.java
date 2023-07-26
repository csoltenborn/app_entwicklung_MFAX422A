package de.fhdw.app_entwicklung.chatgpt.model;

import java.util.Date;

public class Message {
    public final Date date;
    public final Author author;
    public final String message;

    public Message(Author author, String message) {
        this(new Date(), author, message);
    }

    public Message(Date date, Author author, String message) {
        this.date = date;
        this.author = author;
        this.message = message;
    }
}