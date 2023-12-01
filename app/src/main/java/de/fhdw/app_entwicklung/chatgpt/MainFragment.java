package de.fhdw.app_entwicklung.chatgpt;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.fhdw.app_entwicklung.chatgpt.model.Author;
import de.fhdw.app_entwicklung.chatgpt.model.Chat;
import de.fhdw.app_entwicklung.chatgpt.model.Message;
import de.fhdw.app_entwicklung.chatgpt.model.Weather;
import de.fhdw.app_entwicklung.chatgpt.openai.ChatGpt;
import de.fhdw.app_entwicklung.chatgpt.openai.IChatGpt;
import de.fhdw.app_entwicklung.chatgpt.speech.LaunchSpeechRecognition;
import de.fhdw.app_entwicklung.chatgpt.speech.TextToSpeechTool;
import de.fhdw.app_entwicklung.chatgpt.weather.IOpenWeatherMap;
import de.fhdw.app_entwicklung.chatgpt.weather.OpenWeatherMap;
import de.fhdw.app_entwicklung.chatgpt.weather.WeatherPagerAdapter;

public class MainFragment extends Fragment {
    private static final int[] WEATHER_TABS = {R.string.monday, R.string.tuesday, R.string.wednesday, R.string.thursday, R.string.friday, R.string.saturday, R.string.sunday};
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
    private static final String EXTRA_DATA_CHAT = "EXTRA_DATA_CHAT";
    private static final String CHAT_SEPARATOR = "\n\n";

    private TextToSpeechTool textToSpeech;
    private Map<Integer, Chat> chats;
    private IOpenWeatherMap weather;
    private PrefsFacade prefs;

    private final ActivityResultLauncher<LaunchSpeechRecognition.SpeechRecognitionArgs> getTextFromSpeech = registerForActivityResult(
            new LaunchSpeechRecognition(),
            query -> {
                if (query == null || query.isEmpty())
                    return;

                final Chat chat = getCurrentChat();
                final View view = getViewPager().getChildAt(0);
                final TextView textView = view.findViewById(R.id.weatherTextView);
                final ProgressBar progressBar = view.findViewById(R.id.loadingChatBar);

                final Message userMessage = new Message(Author.User, query);
                chat.addMessage(userMessage);

                textView.append(userMessage.toString(getResources()) + CHAT_SEPARATOR);
                progressBar.setVisibility(View.VISIBLE);

                getChatGPT().getChatCompletion(getCurrentChat()).thenAccept(answer -> {
                    MainActivity.uiThreadHandler.post(() -> {
                        textView.append(answer.toString(getResources()) + CHAT_SEPARATOR);
                        progressBar.setVisibility(View.INVISIBLE);

                        textToSpeech.speak(answer.message);
                    });
                });
            });

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

        textToSpeech = new TextToSpeechTool(requireContext(), Locale.getDefault());
        prefs = new PrefsFacade(requireContext());
        weather = new OpenWeatherMap(prefs);
        chats = new HashMap<>();

        initWeatherLayout();

        getAskButton().setOnClickListener(v ->
                getTextFromSpeech.launch(new LaunchSpeechRecognition.SpeechRecognitionArgs(Locale.getDefault())));
        getResetButton().setOnClickListener(v -> resetCurrentChat());
    }

    @Override
    public void onPause() {
        super.onPause();
        textToSpeech.stop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelable(EXTRA_DATA_CHAT, chats);
    }

    @Override
    public void onDestroy() {
        textToSpeech.destroy();
        textToSpeech = null;

        super.onDestroy();
    }

    private void initWeatherLayout() {
        final ViewPager2 viewPager = getViewPager();
        final int dayOfWeek = Helper.getDayOfWeek();
        final TabLayout tabLayout = getTabLayout();

        final WeatherPagerAdapter adapter = new WeatherPagerAdapter(requireActivity(), dayOfWeek, 5);
        viewPager.setAdapter(adapter);

        final TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, (tab, pos) -> {
            final int tabDayOfWeek = (dayOfWeek + pos) % WEATHER_TABS.length;
            tab.setText(WEATHER_TABS[tabDayOfWeek]);
            tab.setTag(tabDayOfWeek);
        });
        tabLayoutMediator.attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state != 0)
                    return;

                loadChat();
            }
        });

        /* ==== Load first chat/forecast when children have been created ==== */
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewPager().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                loadChat();
            }
        });
    }

    private void loadChat() {
        final Chat chat = getCurrentChat();
        final View view = getViewPager().getChildAt(0);
        final TextView weatherTextView = view.findViewById(R.id.weatherTextView);

        if (!chat.hasForcasted) {
            loadWeatherForecast();
            return;
        }

        if (weatherTextView.getText().length() == 0) {
            for (Message msg : chat.getMessages()) {
                if (!msg.printable)
                    continue;

                weatherTextView.append(msg.toString(getResources()) + CHAT_SEPARATOR);
            }
        }
    }

    private void loadWeatherForecast() {
        final Chat chat = getCurrentChat();
        final View view = getViewPager().getChildAt(0);
        final ProgressBar progressBar = view.findViewById(R.id.loadingChatBar);
        final TextView weatherTextView = view.findViewById(R.id.weatherTextView);
        final int tabDayOfWeek = (Integer) getTabLayout().getTabAt(getCurrentChatIndex()).getTag();

        progressBar.setVisibility(View.VISIBLE);
        weather.getWeather(tabDayOfWeek).thenAccept(weatherList -> {
            if (weatherList == null || weatherList.isEmpty()) {
                MainActivity.uiThreadHandler.post(() -> weatherTextView.setText(R.string.noWeatherData));
                progressBar.setVisibility(View.INVISIBLE);
                return;
            }

            int i = 0;
            for (Weather weather : weatherList) {
                chat.addMessage(new Message(Author.System, "Die Wettervorhersage besagt, dass es in " + weather.city + " um " + timeFormat.format(new Date(weather.dt)) + " " + weather.temp + "°C (max:" + weather.tempMax + "/min:" + weather.tempMin + ") sind. Die Luftfeuchtigkeit beträgt "
                        + weather.humidity + "% , die Regenwahrschinlichkeit beträgt " + weather.rainProp + "% und es ist " + weather.cloudiness + "% Bewölkt. Beschreibung: " + weather.description));
                if(i >= 3)
                    break;
                i++;
            }
            chat.addMessage(new Message(Author.User, "Gebe mir bitte eine Aussicht auf das Wetter. Der Text soll: " + prefs.getTextLegth() + " sein und bitte antworte in der Sprache: " + Locale.getDefault().getLanguage(), false));

            getChatGPT().getChatCompletion(chat).thenAccept(answer -> {
                MainActivity.uiThreadHandler.post(() -> {
                    chat.hasForcasted = true;
                    progressBar.setVisibility(View.INVISIBLE);
                    weatherTextView.append(answer + CHAT_SEPARATOR);
                });
            });
        });
    }

    public void resetCurrentChat() {
        final View view = getViewPager().getChildAt(0);
        final TextView weatherTextView = view.findViewById(R.id.weatherTextView);

        weatherTextView.setText("");
        createChat();
        loadWeatherForecast();
    }

    public Chat createChat() {
        final Chat chat = new Chat();
        chats.put(getCurrentChatIndex(), new Chat());
        chat.addMessage(new Message(Author.System, "Stelle dir vor du bist ein Wettermoderator. Sage keinen genauen Zahlen."));
        return chat;
    }

    private Chat getCurrentChat() {
        Chat chat = chats.get(getCurrentChatIndex());
        if (chat == null) {
            chat = createChat();
        }
        return chat;
    }

    public int getCurrentChatIndex() {
        return getTabLayout().getSelectedTabPosition();
    }

    private IChatGpt getChatGPT() {
        return new ChatGpt(prefs.getChatGPTKey());
    }

    private void scrollToEnd(ScrollView scrollView) {
        scrollView.postDelayed(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN), 300);
    }

    private Button getAskButton() {
        return getView().findViewById(R.id.button_ask);
    }

    private Button getResetButton() {
        return getView().findViewById(R.id.button_reset);
    }

    private TabLayout getTabLayout() {
        return getView().findViewById(R.id.weatherLayout);
    }

    private ViewPager2 getViewPager() {
        return getView().findViewById(R.id.viewPager);
    }
}