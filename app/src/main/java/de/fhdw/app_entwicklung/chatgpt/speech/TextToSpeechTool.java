package de.fhdw.app_entwicklung.chatgpt.speech;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Locale;

public class TextToSpeechTool implements TextToSpeech.OnInitListener{
    private final TextToSpeech textToSpeech;
    private boolean ttsAvailable = false;
    private final Locale locale;

    public TextToSpeechTool(@NonNull Context context, Locale locale)
    {
        textToSpeech = new TextToSpeech(context, this);
        this.locale = locale;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(locale);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "TTS: This Language is not supported");
            } else {
                ttsAvailable = true;
            }
        } else {
            Log.e("error", "Failed to initialize TTS");
        }
    }

    public void speak(String text) {
        if (ttsAvailable) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void stop() {
        if (ttsAvailable) {
            textToSpeech.stop();
        }
    }

    public void destroy()
    {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

}