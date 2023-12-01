package de.fhdw.app_entwicklung.chatgpt.model;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class Message implements Parcelable {
    public final Date date;
    public final Author author;
    public final String message;

    public final boolean printable;

    public Message(Author author, String message) {
        this(new Date(), author, message, author != Author.System);
    }

    public Message(Author author, String message, boolean printable) {
        this(new Date(), author, message, printable);
    }

    public Message(Date date, Author author, String message, boolean printable) {
        this.date = date;
        this.author = author;
        this.message = message;
        this.printable = printable;
    }

    protected Message(Parcel in) {
        this(new Date(in.readLong()), Author.valueOf(in.readString()), in.readString(), Boolean.parseBoolean(in.readString()));
    }

    public boolean isUserMessage() {
        return author == Author.User;
    }

    @NonNull
    @Override
    public String toString() {
        return toString(null);
    }

    public String toString(Resources resources) {
        if (resources == null)
            return author.name() + ": " + message;
        return resources.getString(author.ID) + ": " + message;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date.getTime());
        dest.writeString(author.name());
        dest.writeString(message);
        dest.writeString(printable + "");
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