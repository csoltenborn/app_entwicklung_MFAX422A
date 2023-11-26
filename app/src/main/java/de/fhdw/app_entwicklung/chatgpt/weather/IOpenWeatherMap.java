package de.fhdw.app_entwicklung.chatgpt.weather;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import de.fhdw.app_entwicklung.chatgpt.model.Weather;

public interface IOpenWeatherMap {
    CompletableFuture<List<Weather>> getWeather(int dayOfWeek);

    boolean knowsCity(String city);
}
