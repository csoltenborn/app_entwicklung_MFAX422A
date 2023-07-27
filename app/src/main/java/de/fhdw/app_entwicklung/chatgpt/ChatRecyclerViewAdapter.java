package de.fhdw.app_entwicklung.chatgpt;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.fhdw.app_entwicklung.chatgpt.databinding.FragmentChatsBinding;
import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.Message;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    private final List<Chat> mValues;
    private final OnChatClickListener listener;

    public ChatRecyclerViewAdapter(List<Chat> items, OnChatClickListener listener) {
        mValues = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentChatsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Chat chat = mValues.get(position);
        List<Message> messages = chat.getMessages();
        String info = "<no question asked>";
        if (messages.size() > 1) {
            info = messages.get(1).message;
        }

        holder.mItem = chat;
        holder.mIdView.setText(Long.toString(chat.id));
        holder.mContentView.setText(info);
        holder.itemView.setOnClickListener(v -> listener.onChatClick(chat));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public Chat mItem;

        public ViewHolder(FragmentChatsBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}