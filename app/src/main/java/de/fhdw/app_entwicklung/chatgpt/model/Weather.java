package de.fhdw.app_entwicklung.chatgpt.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Weather {
    public final String city;
    public final long dt;
    public final double temp;
    public final double tempMin;
    public final double tempMax;
    public final double humidity;
    public final double rainProp;
    public final double cloudiness;
    public final String description;
    public final boolean isDay;

    public Weather(String city, JSONObject data) throws JSONException {
        this.city = city;
        this.dt = data.optLong("dt") * 1000;

        final JSONObject main = data.getJSONObject("main");
        temp = main.getDouble("temp");
        tempMax = main.getDouble("temp_max");
        tempMin = main.getDouble("temp_min");
        humidity = main.getDouble("humidity");
        rainProp = data.getDouble("pop") / 100;

        final JSONObject clouds = data.optJSONObject("clouds");
        cloudiness = clouds.getDouble("all");

        final JSONObject weather = data.optJSONObject("weather");
        if (weather == null)
            description = "";
        else
            description = weather.optString("description");

        final JSONObject sys = data.optJSONObject("sys");
        isDay = sys.optString("pod").equals("d");
    }
}
