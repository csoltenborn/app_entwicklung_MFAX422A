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

import java.util.List;
import java.util.Locale;

import de.fhdw.app_entwicklung.chatgpt.model.Author;
import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.Message;
import de.fhdw.app_entwicklung.chatgpt.openai.ChatGpt;
import de.fhdw.app_entwicklung.chatgpt.speech.LaunchSpeechRecognition;
import de.fhdw.app_entwicklung.chatgpt.speech.TextToSpeechTool;
import de.fhdw.app_entwicklung.chatgpt.webapi.WebAPI;

public class MainFragment extends Fragment {

    private static final String EXTRA_DATA_CHAT = "EXTRA_DATA_CHAT";
    private static final String CHAT_SEPARATOR = "\n\n";

    private PrefsFacade prefs;
    private TextToSpeechTool textToSpeech;
    private Chat chat;

    private final ActivityResultLauncher<LaunchSpeechRecognition.SpeechRecognitionArgs> getTextFromSpeech = registerForActivityResult(
            new LaunchSpeechRecognition(),
            query -> {
                updateProgressBar(0);
                Message userMessage = new Message(Author.User, query);
                chat.addMessage(userMessage);

                updateTextView(true);
                updateProgressBar(10);

                MainActivity.backgroundExecutorService.execute(() -> {
                    ChatGpt chatGpt = new ChatGpt(prefs.getChatGPTApiToken());
                    String answer = chatGpt.getChatCompletion(chat);
                    updateProgressBar(60);

                    if (answer.contains("api.openweathermap.org")) {
                        try {
                            answer = WebAPI.fetchDataFromApi(answer + "&apikey=" + prefs.getOpenWeatherApiToken() + "&units=metric");
                            updateProgressBar(80);

                            if (!answer.equalsIgnoreCase(WebAPI.ERROR_RESPONSE)) {
                                Message weatherAnswerMessage = new Message(Author.User, answer, Message.MessageType.WeatherAnswer);
                                Chat weatherHelperChat = new Chat();

                                weatherHelperChat.addMessage(weatherAnswerMessage);
                                answer = chatGpt.getChatCompletion(weatherHelperChat);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            answer = WebAPI.ERROR_RESPONSE;
                        }
                    }

                    updateProgressBar(100);
                    Message answerMessage = new Message(Author.Assistant, answer);
                    chat.addMessage(answerMessage);
                    updateTextView();

                    textToSpeech.speak(answer);
                });
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

        prefs = new PrefsFacade(requireContext());
        textToSpeech = new TextToSpeechTool(requireContext(), Locale.GERMAN);
        chat = new Chat();
        if (savedInstanceState != null) {
            chat = savedInstanceState.getParcelable(EXTRA_DATA_CHAT);
        }

        getAskButton().setOnClickListener(v ->
                getTextFromSpeech.launch(new LaunchSpeechRecognition.SpeechRecognitionArgs(Locale.GERMAN)));
        getResetButton().setOnClickListener(v -> {
            chat = new Chat();
            updateTextView();
        });
        updateTextView();
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

    private void updateTextView() {
        updateTextView(false);
    }
    private void updateTextView(boolean showProgressBar) {
        MainActivity.uiThreadHandler.post(() -> {
            getTextView().setText("");
            List<Message> messages = chat.getMessages();
            if (!messages.isEmpty()) {
                getTextView().append(toString(messages.get(0)));
                for (int i = 1; i < messages.size(); i++) {
                    getTextView().append(CHAT_SEPARATOR);
                    getTextView().append(toString(messages.get(i)));
                }

                scrollToEnd();
            }
            getProgressBar().setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
        });
    }

    private void updateProgressBar(int progress) {
        MainActivity.uiThreadHandler.post(() -> {
            getProgressBar().setProgress(progress);
        });
    }

    private void scrollToEnd() {
        getScrollView().postDelayed(() -> 
            getScrollView().fullScroll(ScrollView.FOCUS_DOWN), 300);
    }

    private CharSequence toString(Message message) {
        return message.message;
    }

    private ScrollView getScrollView() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.scrollView);
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

    private ProgressBar getProgressBar() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.progressBar);
    }
}