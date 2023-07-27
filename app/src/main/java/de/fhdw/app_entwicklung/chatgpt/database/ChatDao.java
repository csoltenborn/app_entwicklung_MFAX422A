package de.fhdw.app_entwicklung.chatgpt.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.Message;


@Dao
public abstract class ChatDao {

    @Transaction
    public void insertCompletely(Chat chat) {
        chat.id = insert(chat);

        for (Message message : chat.getMessages()) {
            message.chatId = chat.id;
            message.id = insert(message);
        }
    }

    @Transaction
    public List<Chat> getAllChatsCompletely() {
        List<Chat> allChats = getAllChats();
        for (Chat chat : allChats) {
            for (Message message : getMessagesFromChat(chat.id)) {
                chat.addMessage(message);
            }
        }
        return allChats;
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(Chat chat);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(Message message);

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY date ASC")
    public abstract List<Message> getMessagesFromChat(long chatId);

    @Query("SELECT * FROM chats")
    public abstract List<Chat> getAllChats();

}