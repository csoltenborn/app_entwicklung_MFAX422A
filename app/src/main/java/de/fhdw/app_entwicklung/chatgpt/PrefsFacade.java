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

    public String getApiToken() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("api_token", "");
    }


    public boolean speakOutLoud() {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("read_out_loud", true);
    }

    public Locale getLocale() {
        String language = PreferenceManager.getDefaultSharedPreferences(context).getString("language", "en");
        switch (language) {
            case "de":
                return Locale.GERMANY;
            case "en":
                return Locale.US;
            case "chi":
                return Locale.SIMPLIFIED_CHINESE;
            case "jp":
                return Locale.JAPAN;
            case "ko":
                return Locale.KOREA;
            default:
                throw new RuntimeException("Locale not supported: " + language);
        }
    }

    public String getModel() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("model_type", "gpt-3.5");
    }
}
