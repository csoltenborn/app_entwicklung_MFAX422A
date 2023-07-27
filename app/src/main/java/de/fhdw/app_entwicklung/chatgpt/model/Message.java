package de.fhdw.app_entwicklung.chatgpt.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "messages",
        foreignKeys = {@ForeignKey(entity = Chat.class, parentColumns = "id", childColumns = "chatId")},
        indices = {@Index("chatId")})
public class Message implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public long chatId;

    public final Date date;
    public final Author author;
    public final String message;

    @Ignore
    public Message(Author author, String message) {
        this(new Date(), author, message);
    }

    public Message(Date date, Author author, String message) {
        this.date = date;
        this.author = author;
        this.message = message;
    }

    protected Message(Parcel in) {
        this(new Date(in.readLong()), Author.valueOf(in.readString()), in.readString());
        id = in.readLong();
        chatId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date.getTime());
        dest.writeString(author.name());
        dest.writeString(message);
        dest.writeLong(id);
        dest.writeLong(chatId);
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