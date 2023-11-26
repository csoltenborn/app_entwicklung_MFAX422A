package de.fhdw.app_entwicklung.chatgpt.weather;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import de.fhdw.app_entwicklung.chatgpt.PrefsFacade;
import de.fhdw.app_entwicklung.chatgpt.model.Weather;

public class MockOpenWeatherMap implements IOpenWeatherMap {
    private final PrefsFacade prefs;
    private JSONObject weatherData;

    public MockOpenWeatherMap(PrefsFacade prefs) {
        this.prefs = prefs;
        try {
            weatherData = new JSONObject("{\"dt\":1701021600,\"main\":{\"temp\":5.95,\"feels_like\":3.35,\"temp_min\":5.22,\"temp_max\":5.95,\"pressure\":1014,\"sea_level\":1014,\"grnd_level\":1008,\"humidity\":90,\"temp_kf\":0.73},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"clouds\":{\"all\":83},\"wind\":{\"speed\":3.45,\"deg\":242,\"gust\":7.41},\"visibility\":10000,\"pop\":0.88,\"rain\":{\"3h\":1.16},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2023-11-26 18:00:00\"}");
        } catch (JSONException e) {
            Log.e(MockOpenWeatherMap.class.getSimpleName(), "Could not create mock data", e);
        }
    }

    @Override
    public CompletableFuture<List<Weather>> getWeather(int dayOfWeek) {
        Weather weather = null;
        try {
            weather = new Weather(prefs.getCity(), weatherData);
        } catch (JSONException e) {
            Log.e(MockOpenWeatherMap.class.getSimpleName(), "Could not create mock data", e);
        }

        return CompletableFuture.completedFuture(Collections.singletonList(weather));
    }

    @Override
    public boolean knowsCity(String city) {
        return true;
    }
}
