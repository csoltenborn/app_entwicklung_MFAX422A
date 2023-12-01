package de.fhdw.app_entwicklung.chatgpt;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

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

        prefs = new PrefsFacade(requireContext());
        chat = new Chat();
        if (savedInstanceState != null) {
            chat = savedInstanceState.getParcelable(EXTRA_DATA_CHAT);
        }

        getGenerateButton().setOnClickListener(v -> {
            String query = getQueryInstructions() +
                    generateQueryFromSettings();

            processQuery(query);

            getTextView().append("Configuration Request Details" +
                    generateQueryFromSettings());
        });

        getConfigureButton().setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), PrefsActivity.class);
            startActivity(intent);
        });

        updateTextView();
    }

    private String generateQueryFromSettings() {
        String selectedDistro = prefs.getSelectedDistribution();
        String selectedKernel = prefs.getPrefString("pref_key_kernel", "main");
        String selectedGPU = prefs.getPrefString("pref_key_gpu", "nvidia");
        String selectedDisplayServer = prefs.getPrefString("pref_key_display_server", "wayland");
        String selectedDesktopEnvironment = prefs.getPrefString("pref_key_desktop_environment", "none");
        String selectedLoginManager = prefs.getPrefString("pref_key_login_manager", "ly");
        String selectedWindowManager = prefs.getPrefString("pref_key_window_manager", "hyprland");
        String selectedPackages = prefs.getSelectedPackages();
        String selectedFirewall = prefs.getPrefString("pref_key_firewall", "none");


        return "\n\n\nSelected Distribution: " + selectedDistro + "\n" +
                "Selected Kernel: " + selectedKernel + "\n" +
                "Selected GPU: " + selectedGPU + "\n" +
                "Selected Display Server: " + selectedDisplayServer + "\n" +
                "Selected Desktop Environment: " + selectedDesktopEnvironment + "\n" +
                "Selected Login Manager: " + selectedLoginManager + "\n" +
                "Selected Window Manager: " + selectedWindowManager + "\n" +
                "Selected Packages: " + selectedPackages + "\n" +
                "Selected Firewall: " + selectedFirewall + "\n";
    }

    private void processQuery(String query) {
        String model = prefs.getPrefString("pref_key_model", "gpt-4");
        showProgressBar();
        showTimeDialog(model);
        MainActivity.backgroundExecutorService.execute(() -> {
            Message userMsg = new Message(Author.User, query);
            chat.addMessage(userMsg);

            String apiToken = prefs.getApiToken();
            ChatGpt chatGpt = new ChatGpt(apiToken, model);
            String answer = chatGpt.getChatCompletion(chat);

            Message answerMsg = new Message(Author.Assistant, answer);
            chat.addMessage(answerMsg);

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    hideProgressBar();
                    if (chat.getMessages().size() > 1) {
                        getTextView().append(CHAT_SEPARATOR);
                    }
                    renderTextAsMarkdown(answerMsg.message);
                });
            }
        });
    }


    private String getQueryInstructions() {
        return "Generate a detailed set of Linux installation instructions and, " +
                "if the selected distribution is Arch Linux, include a post-installation script. " +
                "The user's preferences will be specified in the following format:\n" +
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
                "\n" +
                "Based on these preferences, provide a comprehensive guide for installing the specified Linux distribution. " +
                "Include steps for setting up the selected kernel, GPU drivers, display server, desktop environment, and window manager. " +
                "Also, include instructions for installing the specified software packages and configuring the firewall." +
                "For Arch Linux, generate a script for post-installation tasks reflecting the chosen options, " +
                "so that everything that can be installed after installing arch itself, will be installed and set up with said post install script. " +
                "The instructions need to be detailled, the script need to be safe.\n" +
                "Make sure, every step listed in my formatted configuration will be mentioned and explained." +
                "Format your response nicely with headings and whatever you think seems suitable" +
                "The next part of this query will be the selected configuration. Read it like described above.";
    }


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

    private Button getGenerateButton() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.btn_generate);
    }

    private Button getConfigureButton() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.btn_config);
    }

    private ProgressBar getProgressBar() {
        //noinspection ConstantConditions
        return getView().findViewById(R.id.progressBar);
    }

    private void showProgressBar() {
        getProgressBar().setVisibility(View.VISIBLE);
}

    private void hideProgressBar() {
        getProgressBar().setVisibility(View.GONE);
    }
    private void showTimeDialog(String model) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.alert_time)
                .setMessage("This could take a while..." + "\nUsing " + model)
                .setNeutralButton("Okay", null)
                .show();
    }

    private void renderTextAsMarkdown(String markdownText) {
        TextView textView = getTextView();
        Markwon mw = Markwon.create(requireContext());

        mw.setMarkdown(textView, markdownText);
    }


}