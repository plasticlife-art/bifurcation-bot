
package com.math.bifurcation.dto.weather;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "coord",
        "weather",
        "base",
        "main",
        "visibility",
        "wind",
        "clouds",
        "dt",
        "sys",
        "timezone",
        "id",
        "name",
        "cod"
})
public class Weather {

    @JsonProperty("coord")
    public Coord coord;
    @JsonProperty("weather")
    public List<Main> weather = null;
    @JsonProperty("base")
    public String base;
    @JsonProperty("main")
    public CurrentWeather currentWeather;
    @JsonProperty("visibility")
    public Integer visibility;
    @JsonProperty("wind")
    public Wind wind;
    @JsonProperty("clouds")
    public Clouds clouds;
    @JsonProperty("dt")
    public Integer dt;
    @JsonProperty("sys")
    public Sys sys;
    @JsonProperty("timezone")
    public Integer timezone;
    @JsonProperty("id")
    public Integer id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("cod")
    public Integer cod;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "lon",
            "lat"
    })
    public static class Coord {

        @JsonProperty("lon")
        public Integer lon;
        @JsonProperty("lat")
        public Integer lat;

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "all"
    })
    public static class Clouds {

        @JsonProperty("all")
        public Integer all;

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "type",
            "id",
            "country",
            "sunrise",
            "sunset"
    })
    public static class Sys {

        @JsonProperty("type")
        public Integer type;
        @JsonProperty("id")
        public Integer id;
        @JsonProperty("country")
        public String country;
        @JsonProperty("sunrise")
        public Integer sunrise;
        @JsonProperty("sunset")
        public Integer sunset;

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "temp",
            "feels_like",
            "temp_min",
            "temp_max",
            "pressure",
            "humidity"
    })
    public static class CurrentWeather {

        @JsonProperty("temp")
        public Integer temp;
        @JsonProperty("feels_like")
        public Float feelsLike;
        @JsonProperty("temp_min")
        public Integer tempMin;
        @JsonProperty("temp_max")
        public Integer tempMax;
        @JsonProperty("pressure")
        public Integer pressure;
        @JsonProperty("humidity")
        public Integer humidity;

        @Override
        public String toString() {
            return "Main:\n" +
                    "\uD83C\uDF21 temp:        " + temp +
                    " °C\n\uD83D\uDC4C feelsLike:  " + feelsLike +
                    " °C\n\uD83E\uDD76 tempMin: " + tempMin +
                    " °C\n\uD83C\uDF36 tempMax: " + tempMax +
                    " °C\n\uD83C\uDFCB️\u200D♂️ pressure: " + pressure +
                    " мм. рт. ст.\n\uD83D\uDCA7 humidity: " + humidity + "%";
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "speed",
            "deg"
    })
    public static class Wind {

        @JsonProperty("speed")
        public Integer speed;
        @JsonProperty("deg")
        public Integer deg;

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "id",
            "main",
            "description",
            "icon"
    })
    public static class Main {

        @JsonProperty("id")
        public Integer id;
        @JsonProperty("main")
        public String main;
        @JsonProperty("description")
        public String description;
        @JsonProperty("icon")
        public String icon;

    }
}
