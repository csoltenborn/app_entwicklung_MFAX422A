package de.fhdw.app_entwicklung.chatgpt;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

public class PrefsFacade {

    private final Context context;

    public PrefsFacade(@NonNull Context context) {
        this.context = context;
    }

    public String getChatGPTKey() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("gpt_api_token", "");
    }

    public String getOpenWeatherMapKey(){
        return PreferenceManager.getDefaultSharedPreferences(context).getString("weather_api_token", "");
    }

    public String getCity(){
        return PreferenceManager.getDefaultSharedPreferences(context).getString("city", "");
    }
    public String getTextLegth(){
        return PreferenceManager.getDefaultSharedPreferences(context).getString("textLength", "normal");
    }
}
