package de.fhdw.app_entwicklung.chatgpt;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public class PrefsFacade {

    private final Context context;

    public PrefsFacade(@NonNull Context context) {
        this.context = context;
    }

    public String getApiToken() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("api_token", "");
    }

    public String getPrefString(String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
    }

    public Set<String> getStringSet(String key, Set<String> map) {
        return PreferenceManager.getDefaultSharedPreferences(context).getStringSet(key, map);
    }

    public String getSelectedDistribution() {
        String selectedDistro = getPrefString("pref_key_distribution", "arch");
        String customDistro = getPrefString("pref_key_custom_distribution", "");

        if (customDistro.isEmpty()) {
            return selectedDistro;
        }
        return customDistro;
    }

    public String getSelectedPackages() {
        StringBuilder concatenateString = new StringBuilder();

        Set<String> selectedPackages = getStringSet("pref_key_packages", new HashSet<>());

        for (String pkg : selectedPackages) {
            concatenateString.append(pkg).append(", ");
        }

        if (!selectedPackages.isEmpty()) {
            concatenateString.delete(concatenateString.length() - 2, concatenateString.length());
        }

        return concatenateString.toString();
    }

}