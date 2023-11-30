package de.fhdw.app_entwicklung.chatgpt;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class PrefsFacade  {

    private final Context context;


    public PrefsFacade(@NonNull Context context) {
        this.context = context;
    }

    public String getApiToken() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("api_token", "");
    }

    public Locale getLocale() {
        String language = PreferenceManager.getDefaultSharedPreferences(context).getString("language", "de");
        switch (language) {
            case "de":
                return Locale.GERMAN;
            case "en":
                return Locale.ENGLISH;
            case "ita":
                return Locale.ITALIAN;
            case "franc":
                return Locale.FRANCE;
            default:
                throw new RuntimeException("Local not supported: " + language);
        }
    }

    public String getChatgptLanguage() {
        String chatgpt_language = PreferenceManager.getDefaultSharedPreferences(context).getString
                ("chatgpt_language", "English");
        switch(chatgpt_language) {
            case "German":
                return "Übersetzt mir das auf deutsch:";
            case "English":
                return "Translate it in English:";
            case "Italian":
                return "Translate the following to italy:";
            case "French":
                return "Translate the following to france:";
            default:
                throw new RuntimeException("Output Language not supported: " + chatgpt_language);
        }
    }

}