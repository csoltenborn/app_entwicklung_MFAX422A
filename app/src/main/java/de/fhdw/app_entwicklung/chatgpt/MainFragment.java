package de.fhdw.app_entwicklung.chatgpt;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import de.fhdw.app_entwicklung.chatgpt.model.Author;
import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.Message;
import de.fhdw.app_entwicklung.chatgpt.openai.ChatGpt;
import io.noties.markwon.Markwon;

public class MainFragment extends Fragment {

    private static final String EXTRA_DATA_CHAT = "EXTRA_DATA_CHAT";
    private static final String CHAT_SEPARATOR = "\n\n";

    private PrefsFacade prefs;
    private Chat chat;

    private ProgressBar progressBar;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyLanguageSetting();

        prefs = new PrefsFacade(requireContext());
        chat = new Chat();
        if (savedInstanceState != null) {
            chat = savedInstanceState.getParcelable(EXTRA_DATA_CHAT);
        }

        progressBar = getView().findViewById(R.id.progressBar);

        getGenerateButton().setOnClickListener(v -> {
            String query = getQueryInstructions() + generateQueryFromSettings();

            processQuery(query);

            getTextView().append("<h1>Configuration Request Details</h1>\n" + getQueryInstructions() + generateQueryFromSettings());
            //processQuery("Write a simple markdown example template with heading and stuff");
        });

        getConfigureButton().setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), PrefsActivity.class);
            startActivity(intent);
        });

        updateTextView();
    }

    private String generateQueryFromSettings() {
        String selectedDistro = getSelectedDistribution();
        String selectedKernel = getSelectedValueFor("pref_key_kernel", "main");
        String selectedGPU = getSelectedValueFor("pref_key_gpu", "nvidia");
        String selectedDisplayServer = getSelectedValueFor("pref_key_display_server", "wayland");
        String selectedDesktopEnvironment = getSelectedValueFor("pref_key_desktop_environment", "none");
        String selectedLoginManager = getSelectedValueFor("pref_key_login_manager", "ly");
        String selectedWindowManager = getSelectedValueFor("pref_key_window_manager", "hyprland");
        String selectedPackages = getSelectedPackages();
        String selectedFirewall = getSelectedValueFor("pref_key_firewall", "none");


        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("\n\n\nSelected Distribution: ").append(selectedDistro).append("\n");
        queryBuilder.append("Selected Kernel: ").append(selectedKernel).append("\n");
        queryBuilder.append("Selected GPU: ").append(selectedGPU).append("\n");
        queryBuilder.append("Selected Display Server: ").append(selectedDisplayServer).append("\n");
        queryBuilder.append("Selected Desktop Environment: ").append(selectedDesktopEnvironment).append("\n");
        queryBuilder.append("Selected Login Manager: ").append(selectedLoginManager).append("\n");
        queryBuilder.append("Selected Window Manager: ").append(selectedWindowManager).append("\n");
        queryBuilder.append("Selected Packages: ").append(selectedPackages).append("\n");
        queryBuilder.append("Selected Firewall: ").append(selectedFirewall).append("\n");

        return queryBuilder.toString();
    }

    private void processQuery(String query) {
        showProgressBar();
        showTimeDialog();
        MainActivity.backgroundExecutorService.execute(() -> {
            Message userMsg = new Message(Author.User, query);
            chat.addMessage(userMsg);

            String apiToken = prefs.getApiToken();
            ChatGpt chatGpt = new ChatGpt(apiToken);
            String answer = chatGpt.getChatCompletion(chat);

            Message answerMsg = new Message(Author.Assistant, answer);
            chat.addMessage(answerMsg);

            // Use runOnUiThread for UI updates
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    hideProgressBar();
                    if (chat.getMessages().size() > 1) {
                        getTextView().append(CHAT_SEPARATOR);
                    }
                    //getTextView().append(toString(answerMsg));
                    displayResponseAsMarkdown(answerMsg.message);
                    //ScrollView scrollView = getScrollView();
                    //scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
                });
            }
        });
    }


    private String getQueryInstructions() {
        String customInstructions = "Generate a detailed set of Linux installation instructions and, if the selected distribution is Arch Linux, include a post-installation script. The user's preferences will be specified in the following format:\n" +
                "\n" +
                "Selected Distribution: <distribution_name>\n" +
                "Selected Kernel: <kernel_type>\n" +
                "GPU Preference: <GPU_type>\n" +
                "Display Server: <display_server_type>\n" +
                "Desktop Environment: <desktop_environment>\n" +
                "Login Manager (if applicable): <login_manager>\n" +
                "Window Manager: <window_manager>\n" +
                "Software Packages: <list_of_software_packages>\n" +
                "Firewall: <firewall_type>\n" +
                "Autostart Settings: <autostart_settings>\n" +
                "\n" +
                "Based on these preferences, provide a comprehensive guide for installing the specified Linux distribution. Include steps for setting up the selected kernel, GPU drivers, display server, desktop environment, and window manager. Also, include instructions for installing the specified software packages and configuring the firewall. For Arch Linux, generate a script for post-installation tasks reflecting the chosen options, so that everything that can be installed after installing arch itself, will be installed and set up with said post install script. The instructions need to be detailled, the script need to be safe.\n";
        return customInstructions;
    }

    // Move to PrefsFacade
    private String getSelectedDistribution() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String selectedDistro = sharedPref.getString("pref_key_distribution", "arch");
        String customDistro = sharedPref.getString("pref_key_custom_distribution", "");

        if (customDistro.isEmpty()) {
            return selectedDistro;
        }
        return customDistro;
    }

    private String getSelectedValueFor(String key, String defaultValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String valueForKey = sharedPref.getString(key, defaultValue);

        return valueForKey;
    }

    private String getSelectedPackages() {
        StringBuilder concatenateString = new StringBuilder();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext());
        Set<String> selectedPackages = sharedPref.getStringSet("pref_key_packages", new HashSet<>());
        String selectedPackagesString;

        for (String pkg : selectedPackages) {
            concatenateString.append(pkg).append(", ");
        }

        if (!selectedPackages.isEmpty()) {
            concatenateString.delete(concatenateString.length() - 2, concatenateString.length());
        }

        // Convert Set to array for String.join()
        return concatenateString.toString();
    }

    private void applyLanguageSetting() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
    String languageCode = prefs.getString("pref_key_language", "default"); // default is your default language code
    setLocale(languageCode);
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getContext().getResources();
        Configuration config = resources.getConfiguration();
        config.locale = locale;
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    // Remove
    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_DATA_CHAT, chat);
    }

    // Remove
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void updateTextView() {
        getTextView().setText("");
        List<Message> messages = chat.getMessages();
        if (!messages.isEmpty()) {
            getTextView().append(toString(messages.get(0)));
            for (int i = 1; i < messages.size(); i++) {
                getTextView().append(CHAT_SEPARATOR);
                getTextView().append(toString(messages.get(i)));
            }
        }
    }

    private CharSequence toString(Message message) {
        return message.message;
    }

    private TextView getTextView() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.textView);
    }

    private ScrollView getScrollView() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.scrollView);
    }

    private Button getGenerateButton() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.btn_generate);
    }

    private Button getConfigureButton() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.btn_config);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
}

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
    private void showTimeDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.alert_time)
                .setMessage(R.string.alert_time_message)
                .setNeutralButton("Okay", null)
                .show();
    }

    private void displayResponseAsMarkdown(String markdownText) {
        TextView textView = getTextView();
        Markwon mw = Markwon.create(requireContext());

        mw.setMarkdown(textView, markdownText);
    }


}