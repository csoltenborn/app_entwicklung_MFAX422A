package de.fhdw.app_entwicklung.chatgpt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chat implements Parcelable {

    private final List<Message> messages = new ArrayList<>();
    public boolean hasForcasted;

    public Chat() {
    }

    protected Chat(Parcel in) {
        in.readList(messages, Message.class.getClassLoader());
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public void reset() {
        messages.clear();
    }

    /**
     * @return True when any other message then {@link Author#User} are present
     */
    public boolean hasMessages() {
        for (Message msg : messages) {
            if (msg.author != Author.System)
                return true;
        }
        return false;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(messages);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };
}