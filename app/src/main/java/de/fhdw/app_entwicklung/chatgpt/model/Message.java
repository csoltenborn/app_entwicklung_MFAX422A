package de.fhdw.app_entwicklung.chatgpt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Message implements Parcelable {
    public final Date date;
    public final Author author;
    public String message;
    public MessageType messageType;

    public enum MessageType {
        Normal,
        System,
        WeatherAnswer,
        WeatherQuery
    }

    public Message(Author author, String message) {
        this(new Date(), author, message);
    }

    public Message(Date date, Author author, String message) {
        this.date = date;
        this.author = author;
        this.message = message;
        this.messageType = MessageType.Normal;
    }

    public Message(Author author, String message, MessageType msgType) {
        this.date = new Date();
        this.author = author;
        this.message = message;
        this.messageType = msgType;
    }

    public Message(Date date, Author author, String message, MessageType msgType) {
        this.date = date;
        this.author = author;
        this.message = message;
        this.messageType = msgType;
    }

    protected Message(Parcel in) {
        this(new Date(in.readLong()), Author.valueOf(in.readString()), in.readString(), MessageType.valueOf(in.readString()));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date.getTime());
        dest.writeString(author.name());
        dest.writeString(message);
        dest.writeString(messageType.name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}