package com.math.bifurcation.api.client;

import com.math.bifurcation.dto.weather.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Leonid Cheremshantsev
 */
@Service
public class WeatherClient extends ApiClient {

    public static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=Samara&appid=%s&units=metric";
    private final String token;

    public WeatherClient(@Value("${weather.token}") String token) {
        super();
        this.token = token;
    }

    Weather getWeather() {
        return get(weatherUrl(), Weather.class);
    }

    public Weather.CurrentWeather getCurrentWeather() {
        return getWeather().currentWeather;
    }

    private String weatherUrl() {
        return String.format(API_URL, token);
    }

}
