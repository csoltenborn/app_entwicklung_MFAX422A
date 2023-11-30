package de.fhdw.app_entwicklung.chatgpt;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
                Message userMessage = new Message(Author.User,"Text: \n"+ query);
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

                    Message answerMessage = new Message(Author.Assistant,"ChatGPT: \n"+ answer);
                    chat.addMessage(answerMessage);

                    MainActivity.uiThreadHandler.post(() -> {
                        getTextView().append(CHAT_SEPARATOR);
                        getTextView().append(toString(answerMessage));
                        scrollToEnd();
                        textToSpeech.speak(answer);
                    });
                });
            });

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    getImageView().setImageURI(imageUri);
                    processImage(imageUri);
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        getScanButton().setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);

        });

        updateTextView();
    }

    /*
     * Die Methode processImage(Uri imageUri) verarbeitet ein ausgewähltes Bild, um den enthaltenen Text zu extrahieren und mit ChatGPT zu kommunizieren.
     */
    private void processImage(Uri imageUri) {
        try {
            // Das ausgewählte Bild wird in ein Bitmap umgewandelt.
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);

            // Ein FirebaseVisionImage wird aus dem Bitmap erstellt.
            FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);

            // Ein TextRecognizer von Firebase Vision wird initialisiert.
            FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

            // Der TextRecognizer verarbeitet das Bild, und das Ergebnis wird in einem Task gespeichert.
            Task<FirebaseVisionText> result = textRecognizer.processImage(firebaseVisionImage);

            result.addOnSuccessListener(firebaseVisionText -> {
                // Der extrahierte Text wird abgerufen und als Benutzer-Nachricht hinzugefügt.
                String extractedText = firebaseVisionText.getText();
                Message imageMessage = new Message(Author.User, "Text: \n" + extractedText);
                chat.addMessage(imageMessage);

                // Falls bereits Nachrichten vorhanden sind, wird ein Trennstrich hinzugefügt.
                if (chat.getMessages().size() > 1) {
                    getTextView().append(CHAT_SEPARATOR);
                }

                // Die Benutzer-Nachricht wird zur Anzeige hinzugefügt.
                getTextView().append(toString(imageMessage));
                scrollToEnd();

                // Ein Hintergrund-Thread wird gestartet, um mit ChatGPT zu kommunizieren.
                MainActivity.backgroundExecutorService.execute(() -> {
                    // Ein API-Token für die ChatGPT-Kommunikation wird definiert.
                    String apiToken = prefs.getApiToken();
                    ChatGpt chatGpt = new ChatGpt(apiToken);

                    // Die Kommunikation mit ChatGPT erfolgt, und die Antwort wird als ChatGPT-Nachricht hinzugefügt.
                    String answer = chatGpt.getChatCompletion(chat);
                    Message answerMessage = new Message(Author.Assistant, "ChatGPT: \n" + answer);
                    chat.addMessage(answerMessage);

                    // Die Antwort von ChatGPT wird zur Anzeige hinzugefügt, gescrollt und vorgelesen.
                    MainActivity.uiThreadHandler.post(() -> {
                        getTextView().append(CHAT_SEPARATOR);
                        getTextView().append(toString(answerMessage));
                        scrollToEnd();
                        textToSpeech.speak(answer);
                    });
                });
            });

        } catch (Exception e) {
            // Im Falle einer Ausnahme wird die Fehlermeldung ausgegeben.
            e.printStackTrace();
        }
    }



    private void updateTextView() {
        getTextView().setText("");
        getImageView().setImageURI(null);
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


    private void scrollToEnd() {
        getScrollView().postDelayed(() -> getScrollView().fullScroll(ScrollView.FOCUS_DOWN), 300);
    }

    private CharSequence toString(Message message) {
        return message.message;
    }

    private TextView getTextView() {
        return getView().findViewById(R.id.textView);
    }

    private Button getAskButton() {
        return getView().findViewById(R.id.button_ask);
    }

    private Button getResetButton() {
        return getView().findViewById(R.id.button_reset);
    }

    private Button getScanButton() {
        return getView().findViewById(R.id.camera);
    }

    private ScrollView getScrollView() {
        return getView().findViewById(R.id.scrollview);
    }

    private ImageView getImageView() {
        return getView().findViewById(R.id.image);
    }
}