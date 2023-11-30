package de.fhdw.app_entwicklung.chatgpt;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Set;


// Save Application Context
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


            ListPreference distributionPref = findPreference("pref_key_distribution");
            EditTextPreference customDistroPref = findPreference("pref_key_custom_distribution");

            MultiSelectListPreference packagesPref = findPreference("pref_key_packages");


            // Handle the custom distro field, if custom is selected
            if (distributionPref != null && customDistroPref != null) {
                distributionPref.setOnPreferenceChangeListener(((preference, newValue) -> {
                    boolean isCustomDistro = "custom".equals(newValue);
                    customDistroPref.setVisible("custom".equals(newValue));

                    if (!isCustomDistro) {
                        customDistroPref.setText("");
                    }

                    return true;
                }));
            }

            // Custom Summary Provider for packages
            if (packagesPref != null) {
                packagesPref.setSummaryProvider(preference -> {
                    Set<String> selectedValues = ((MultiSelectListPreference) preference).getValues();
                    if (selectedValues.isEmpty()){
                        return "No packages selected";
                    } else {
                        // Convert Set to array for String.join()
                        String[] selectedValuesArr = selectedValues.toArray(new String[0]);
                        return String.join(", ", selectedValuesArr);
                    }
                });
            }

        }
        @Override
        public void onNavigateToScreen(PreferenceScreen preferenceScreen) {
            // Create new instance of fragment with specified preference screen
            SettingsFragment fragment = new SettingsFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PREFERENCE_ROOT, preferenceScreen.getKey());
            fragment.setArguments(args);

            // Replace current fragment with new fragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.settings, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

}