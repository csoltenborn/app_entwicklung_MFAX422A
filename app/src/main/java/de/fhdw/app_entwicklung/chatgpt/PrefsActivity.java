package de.fhdw.app_entwicklung.chatgpt;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;

public class PrefsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        newBase.getResources().getConfiguration().setLocale(Locale.getDefault());
        Log.i("Test", Locale.getDefault()+"");
        super.attachBaseContext(newBase);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            ListPreference sprache = findPreference("language");
            if (sprache != null){
                sprache.setOnPreferenceChangeListener((preference, newValue) -> {
                    if (newValue == null || newValue.toString().isEmpty()){
                        return true;
                    }
                    Locale locale = Locale.forLanguageTag(newValue.toString());

                    if (locale == null){
                        return true;
                    }

                    Locale.setDefault(locale);

                    getContext().getApplicationContext().getResources().getConfiguration().setLocale(locale);
                    getActivity().recreate();
                    return true;


                });

            }
        }
    }
}