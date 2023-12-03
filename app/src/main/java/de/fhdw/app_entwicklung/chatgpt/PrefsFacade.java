package de.fhdw.app_entwicklung.chatgpt;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class PrefsFacade {

    private final Context context;

    public PrefsFacade(@NonNull Context context) {
        this.context = context;
    }

    public boolean speakOutLoud() {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("read_out_loud", true);
    }

    public String getApiToken() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("api_token", "");
    }

    public Locale getLocale() {
        String language = PreferenceManager.getDefaultSharedPreferences(context).getString("language", "en");
        switch (language) {
            case "de":
                return Locale.GERMANY;
            case "en":
                return Locale.US;
            default:
                throw new RuntimeException("Locale not supported: " + language);
        }
    }

    public String getSpeakingStyle() {
        String style = PreferenceManager.getDefaultSharedPreferences(context).getString("speaking_style", "neutral");
        switch (style) {
            case "neutral":
                return "";
            case "dog":
                return "Speak in the style of a dog!";
            case "trump":
                return "Speak in the style of Donald Trump!";
            case "shakespeare":
                return "Speak in the style of William Shakespeare!";
            case "custom":
                return PreferenceManager.getDefaultSharedPreferences(context).getString("custom_speaking_style", "");
            default:
                throw new RuntimeException("Speaking style not supported: " + style);
        }
    }

    public String getModel() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("model_type", "gpt-3.5-turbo");
    }

}