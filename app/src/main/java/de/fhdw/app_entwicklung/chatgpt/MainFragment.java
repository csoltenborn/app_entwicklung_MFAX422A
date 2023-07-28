package de.fhdw.app_entwicklung.chatgpt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import java.util.List;
import java.util.stream.Collectors;

import de.fhdw.app_entwicklung.chatgpt.database.AppDatabase;
import de.fhdw.app_entwicklung.chatgpt.model.Author;
import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.Message;
import de.fhdw.app_entwicklung.chatgpt.openai.IChatGpt;
import de.fhdw.app_entwicklung.chatgpt.openai.MockChatGpt;
import de.fhdw.app_entwicklung.chatgpt.speech.LaunchSpeechRecognition;
import de.fhdw.app_entwicklung.chatgpt.speech.TextToSpeechTool;

public class MainFragment extends Fragment {

    private static final String EXTRA_DATA_CHAT = "EXTRA_DATA_CHAT";
    private static final String CHAT_SEPARATOR = "\n\n";

    private static AppDatabase DATABASE = null;
    public static AppDatabase getDatabase() {
        return DATABASE;
    }

    private PrefsFacade prefs;
    private TextToSpeechTool textToSpeech;
    private Chat chat;

    private final ActivityResultLauncher<LaunchSpeechRecognition.SpeechRecognitionArgs> getTextFromSpeech = registerForActivityResult(
            new LaunchSpeechRecognition(),
            query -> {
                if (query == null || query.trim().isEmpty()) {
                    return;
                }
                askChatGtp(query);
            });

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

        if (DATABASE == null) {
            //noinspection ConstantConditions
            DATABASE = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "chat_db").build();
        }

        prefs = new PrefsFacade(requireContext());
        textToSpeech = new TextToSpeechTool(requireContext(), prefs.getLocale());

        if (savedInstanceState != null) {
            setChat(savedInstanceState.getParcelable(EXTRA_DATA_CHAT));
        } else {
            setChat(createChat());
        }

        getAskButton().setOnClickListener(v ->
                getTextFromSpeech.launch(new LaunchSpeechRecognition.SpeechRecognitionArgs(prefs.getLocale())));
        getResetButton().setOnClickListener(v -> setChat(createChat()));
    }

    @Override
    public void onPause() {
        super.onPause();

        textToSpeech.stop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_DATA_CHAT, chat);
    }

    @Override
    public void onDestroy() {
        textToSpeech.destroy();
        textToSpeech = null;

        super.onDestroy();
    }

    public void setChat(Chat chat) {
        this.chat = chat;
        updateTextView();
        scrollToEnd();
        updateButtonStates();
    }

    @NonNull
    public CharSequence getChatText() {
        return getTextView().getText();
    }

    private void askChatGtp(@NonNull String query) {
        disableButtons();
        getProgressBar().setVisibility(View.VISIBLE);

        Message userMessage = new Message(Author.User, query);
        chat.addMessage(userMessage);
        if (chat.getMessages().size() > 2) {
            getTextView().append(CHAT_SEPARATOR);
        }
        getTextView().append(toString(userMessage));
        scrollToEnd();

        MainActivity.backgroundExecutorService.execute(() -> {
            IChatGpt chatGpt = createChatGtp();
            String answer = chatGpt.getChatCompletion(chat);

            Message answerMessage = new Message(Author.Assistant, answer);
            chat.addMessage(answerMessage);

            getDatabase().chatDao().insertCompletely(chat);

            MainActivity.uiThreadHandler.post(() -> {
                getTextView().append(CHAT_SEPARATOR);
                getTextView().append(toString(answerMessage));
                scrollToEnd();
                getProgressBar().setVisibility(View.GONE);
                updateButtonStates();
                if (prefs.speakOutLoud()) {
                    textToSpeech.speak(answer);
                }
            });
        });
    }

    @NonNull
    protected IChatGpt createChatGtp() {
        return new MockChatGpt();
        //return new ChatGpt(prefs.getApiToken(), prefs.getModel());
    }

    private void updateTextView() {
        getTextView().setText("");
        List<Message> messages = chat.getMessages().stream()
                .filter(message -> message.author == Author.User || message.author == Author.Assistant)
                .collect(Collectors.toList());
        if (!messages.isEmpty()) {
            getTextView().append(toString(messages.get(0)));
            for (int i = 1; i < messages.size(); i++) {
                getTextView().append(CHAT_SEPARATOR);
                getTextView().append(toString(messages.get(i)));
            }
        }
        scrollToEnd();
    }
    
    private void updateButtonStates() {
        getAskButton().setEnabled(true);
        getResetButton().setEnabled(getTextView().getText().length() > 0);
    }
    
    private void disableButtons() {
        getAskButton().setEnabled(false);
        getResetButton().setEnabled(false);
    }

    private void scrollToEnd() {
        getScrollView().postDelayed(() -> getScrollView().fullScroll(ScrollView.FOCUS_DOWN), 300);
    }

    private Chat createChat() {
        Chat chat = new Chat();
        chat.addMessage(new Message(Author.System, prefs.getSpeakingStyle()));
        return chat;
    }

    private CharSequence toString(Message message) {
        return message.message != null ? message.message : "";
    }

    private TextView getTextView() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.textView);
    }

    private Button getAskButton() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.button_ask);
    }

    private Button getResetButton() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.button_reset);
    }

    private ScrollView getScrollView() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.scrollview);
    }

    private ProgressBar getProgressBar() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.progressBar);
    }

}