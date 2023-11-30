package de.fhdw.app_entwicklung.chatgpt;

import android.content.SharedPreferences;
import android.content.res.Resources;
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
import androidx.preference.PreferenceManager;

import java.util.List;

import de.fhdw.app_entwicklung.chatgpt.model.Author;
import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.Message;
import de.fhdw.app_entwicklung.chatgpt.openai.ChatGpt;
import de.fhdw.app_entwicklung.chatgpt.speech.LaunchSpeechRecognition;
import de.fhdw.app_entwicklung.chatgpt.speech.TextToSpeechTool;

public class MainFragment extends Fragment {

    private static final String EXTRA_DATA_CHAT = "EXTRA_DATA_CHAT";
    private static final String CHAT_SEPARATOR = "\n\n";

    private PrefsFacade prefs;
    private TextToSpeechTool textToSpeech;
    private Chat chat;

    private final ActivityResultLauncher<LaunchSpeechRecognition.SpeechRecognitionArgs> getTextFromSpeech = registerForActivityResult(
            new LaunchSpeechRecognition(),
            query -> {
                Message userMessage = new Message(Author.User, query);
                chat.addMessage(userMessage);
                if (chat.getMessages().size() > 1) {
                    getTextView().append(CHAT_SEPARATOR);
                }
                getTextView().append(toString(userMessage));
                scrollToEnd();

                MainActivity.backgroundExecutorService.execute(() -> {
                    String apiToken = prefs.getApiToken();
                    ChatGpt chatGpt = new ChatGpt(apiToken);
                    String answer = chatGpt.getChatCompletion(chat);

                    Message answerMessage = new Message(Author.Assistant, answer);
                    chat.addMessage(answerMessage);

                    MainActivity.uiThreadHandler.post(() -> {
                        getTextView().append(CHAT_SEPARATOR);
                        getTextView().append(toString(answerMessage));
                        scrollToEnd();

                        //Für einstellung ob es gesagt+geschriben oder nur geschriben werden soll
                        if (prefs.speakOutLoud()) {
                            // Setze die Sprache in der TextToSpeechTool-Klasse
                            textToSpeech.setLanguage(prefs.getLocale());
                            // Führe die Sprachausgabe durch
                            textToSpeech.speak(answer);

                        }
                    });
                });
            });

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_main, container, false);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Neue Codezeilen für die Spracheinstellungen
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String selectedLanguage = preferences.getString("language", "default_language_value");

        // Beispiel: Verwende die ausgewählte Sprache, um die korrekte Ressource zu laden
        int askResourceId = getResourceId("ask", selectedLanguage);
        String askText = getString(askResourceId);

        // Beispiel: Aktualisiere eine TextView mit dem ausgewählten Text
        TextView textView = rootView.findViewById(R.id.textView);
        textView.setText(askText);
        return rootView;
    }


    // Methode, um die Ressourcen-ID für eine bestimmte Zeichenkette in einer bestimmten Sprache zu erhalten
    private int getResourceId(String resourceName, String language) {
        Resources resources = getResources();
        String packageName = getActivity().getPackageName();
        int resourceId = resources.getIdentifier(resourceName, "string", packageName);

        // Wenn die Ressource für die ausgewählte Sprache nicht gefunden wird, verwende die Standard-Ressource
        if (resourceId == 0) {
            resourceId = resources.getIdentifier(resourceName, "string", packageName);
        }

        return resourceId;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Gerätstandart Sprache als Sprache aus wählen
        prefs = new PrefsFacade(requireContext());
        textToSpeech = new TextToSpeechTool(requireContext(), prefs.getLocale());

        //New
        getAskButton().setOnClickListener(v ->
                getTextFromSpeech.launch(new LaunchSpeechRecognition.SpeechRecognitionArgs(prefs.getLocale())));


        chat = new Chat();
        if (savedInstanceState != null) {
            chat = savedInstanceState.getParcelable(EXTRA_DATA_CHAT);
        }

        getAskButton().setOnClickListener(v ->
                //Gerätstandart Sprache als Sprache aus wählen
                getTextFromSpeech.launch(new LaunchSpeechRecognition.SpeechRecognitionArgs(prefs.getLocale())));
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
        getTextView().setText("");
        List<Message> messages = chat.getMessages();
        if (!messages.isEmpty()) {
            getTextView().append(toString(messages.get(0)));
            for (int i = 1; i < messages.size(); i++) {
                getTextView().append(CHAT_SEPARATOR);
                getTextView().append(toString(messages.get(i)));
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