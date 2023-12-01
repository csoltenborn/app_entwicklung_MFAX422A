package de.fhdw.app_entwicklung.chatgpt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import de.fhdw.app_entwicklung.chatgpt.model.email.SendEmail;
import de.fhdw.app_entwicklung.chatgpt.openai.ChatGpt;
import de.fhdw.app_entwicklung.chatgpt.openai.IChatGpt;
import de.fhdw.app_entwicklung.chatgpt.speech.LaunchSpeechRecognition;
import de.fhdw.app_entwicklung.chatgpt.speech.TextToSpeechTool;

public class MainFragment extends Fragment {

    private static final String EXTRA_DATA_CHAT = "EXTRA_DATA_CHAT";
    private static final String CHAT_SEPARATOR = "\n\n";

    public static PrefsFacade prefs;
    private TextToSpeechTool textToSpeech;
    private Chat chat;

    private final ActivityResultLauncher<LaunchSpeechRecognition.SpeechRecognitionArgs> getTextFromSpeech = registerForActivityResult(
            new LaunchSpeechRecognition(),
            query -> {
                Message userMessage = new Message(Author.User, "Schreib mir bitte eine Email: " + query);
                chat.addMessage(userMessage);
                getTextView().append(toString(userMessage));
                getTextView().append(CHAT_SEPARATOR);
                scrollToEnd();

                MainActivity.backgroundExecutorService.execute(() -> {
                    String apiToken = prefs.getApiToken();
                    IChatGpt chatGpt = new ChatGpt(apiToken);
                    String answer = chatGpt.getChatCompletion(chat);

                    Message answerMessage = new Message(Author.Assistant, answer);
                    chat.addMessage(answerMessage);

                    MainActivity.uiThreadHandler.post(() -> {
                        int endIndex = answer.indexOf("\n");
                        int startIndex = answer.indexOf(":") +2;
                        if(endIndex < 0 || startIndex < 0){
                            return;
                        }
                        String subject = answer.substring(startIndex,endIndex);
                        String message = answer.substring(endIndex+2);
                        SendEmail sendEmail = new SendEmail(prefs.getEmailTo(),subject,message);
                        sendEmail.launchEmail(getContext());

                        getTextView().append(toString(answerMessage));
                        getTextView().append(CHAT_SEPARATOR);
                        scrollToEnd();
                        textToSpeech.speak(answer);
                    });
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
        createChat();
        if (savedInstanceState != null) {
            chat = savedInstanceState.getParcelable(EXTRA_DATA_CHAT);
        }

        getAskButton().setOnClickListener(v ->
                getTextFromSpeech.launch(new LaunchSpeechRecognition.SpeechRecognitionArgs(Locale.GERMAN)));
        getResetButton().setOnClickListener(v -> {
            createChat();
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
    public void createChat(){
        chat = new Chat();
        String msg =
                "Die erste Zeile der Email soll der Betreff sein. " +
                "Die Email darf max. 2-3 Zeilen lang sein." +
                "Die Email soll formal geschrieben sein. " +
                "Der Verfasser der Email bin ich:";

        chat.addMessage(new Message(Author.System,msg + prefs.getEmailAuthor()));
    }
    private void updateTextView() {
        getTextView().setText("");
        List<Message> messages = chat.getMessages();
        if (!messages.isEmpty()) {
            for (int i = 0; i < messages.size(); i++) {
                Message message = messages.get(i);
                if(message.isPrintable()){
                    getTextView().append(toString(messages.get(i)));
                    getTextView().append(CHAT_SEPARATOR);
                }
            }
        }
        scrollToEnd();
    }

    private void scrollToEnd() {
        getScrollView().postDelayed(() -> getScrollView().fullScroll(ScrollView.FOCUS_DOWN), 300);
    }

    private CharSequence toString(Message message) {
        return message.message;
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

}