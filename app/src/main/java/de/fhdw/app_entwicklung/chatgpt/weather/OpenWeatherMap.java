package de.fhdw.app_entwicklung.chatgpt.weather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import de.fhdw.app_entwicklung.chatgpt.Helper;
import de.fhdw.app_entwicklung.chatgpt.MainActivity;
import de.fhdw.app_entwicklung.chatgpt.PrefsFacade;
import de.fhdw.app_entwicklung.chatgpt.model.Weather;

public class OpenWeatherMap implements IOpenWeatherMap {
    private static final long cacheInvalidMillis = 3 * 60 * 1000;
    private final PrefsFacade prefs;

    private List<Weather> weatherCache;
    private String lastCity;
    private long lastMillis;

    public OpenWeatherMap(PrefsFacade prefs) {
        this.weatherCache = new ArrayList<>();
        this.prefs = prefs;
    }

    @Override
    public CompletableFuture<List<Weather>> getWeather(int dayOfWeek) {
        return CompletableFuture.supplyAsync(() -> getByDayOfWeek(dayOfWeek)
                , MainActivity.backgroundExecutorService).exceptionally(tr -> {
            Log.e(OpenWeatherMap.class.getSimpleName(), "Could not get weather data.", tr);
            return null;
        });
    }

    private List<Weather> getByDayOfWeek(int dayOfWeek) {
        if (weatherCache.isEmpty() || System.currentTimeMillis() - lastMillis >= cacheInvalidMillis || !lastCity.equals(prefs.getCity()))
            fetchWeatherData();

        final List<Weather> res = new ArrayList<>();
        for (Weather weather : weatherCache) {
            final int weatherDayOfWeek = Helper.getDayOfWeek(weather.dt);
            if (weatherDayOfWeek == dayOfWeek)
                res.add(weather);
        }
        return res;
    }

    public void fetchWeatherData() {
        final String res = getResponse(getBaseUrl(true));
        if (res == null || res.isEmpty())
            return;

        try {
            final JSONObject response = new JSONObject(res);
            final JSONArray array = response.getJSONArray("list");

            lastMillis = System.currentTimeMillis();
            lastCity = prefs.getCity();
            weatherCache.clear();

            for (int i = 0; i < array.length(); i++) {
                final Weather weather = new Weather(prefs.getCity(), array.getJSONObject(i));
                weatherCache.add(weather);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean knowsCity(String city) {
        final String res = getResponse(getBaseUrl(false));
        if (res == null || res.isEmpty())
            return false;

        try {
            final JSONObject response = new JSONObject(res);
            return response.optInt("cod", 404) == 200;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getResponse(String urlS) {
        try {
            final URL url = new URL(urlS);

            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setRequestMethod("GET");

            final StringBuilder buffer = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null)
                    buffer.append(line);
            } finally {
                connection.disconnect();
            }

            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getBaseUrl(boolean forecast) {
        final String forecastStr = forecast ? "forecast" : "weather";
        return "https://api.openweathermap.org/data/2.5/" + forecastStr + "?q=" + prefs.getCity() + "&units=metric&lang=" + Locale.getDefault().getLanguage() + "&appid=" + prefs.getOpenWeatherMapKey();
    }
}