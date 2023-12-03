package de.fhdw.app_entwicklung.chatgpt.speech;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class LaunchSpeechRecognition extends ActivityResultContract<LaunchSpeechRecognition.SpeechRecognitionArgs, String> {

    public static class SpeechRecognitionArgs
    {
        public final Locale locale;
        public final String prompt;

        public SpeechRecognitionArgs(Locale locale)
        {
            this(locale, null);
        }

        public SpeechRecognitionArgs(Locale locale, String prompt)
        {
            this.locale = locale;
            this.prompt = prompt;
        }
    }

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, SpeechRecognitionArgs args) {
        Intent intent
                = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                args.locale.toString());
        if (args.prompt != null) {
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, args.prompt);
        }
        return intent;
    }

    @Override
    public String parseResult(int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            return Objects.requireNonNull(result).get(0);
        }
        return null;
    }
}