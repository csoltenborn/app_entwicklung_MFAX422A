package de.fhdw.app_entwicklung.chatgpt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import de.fhdw.app_entwicklung.chatgpt.openai.ChatGpt;

public class MainFragment extends Fragment {

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getButton().setOnClickListener(v -> MainActivity.backgroundExecutorService.execute(() ->
        {
            ChatGpt chatGpt = new ChatGpt("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");
            String answer = chatGpt.getChatCompletion("What's the answer to the universe and everything?");
            getTextView().setText(answer);
        }));
    }

    private TextView getTextView() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.textView);
    }

    private Button getButton() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.button_ask);
    }

}