package de.fhdw.app_entwicklung.chatgpt;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

public class PrefsFacade {

    private final Context context;

    public PrefsFacade(@NonNull Context context) {
        this.context = context;
    }

    public String getApiToken() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("api_token", "");
    }

}