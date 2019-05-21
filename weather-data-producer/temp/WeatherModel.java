package wg.weather.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherModel {

    @JsonProperty("coord")
    private Coord coord;

    @JsonProperty("weather")
    private List<Weather> weatherList;

    @JsonProperty("base")
    private String base;

    @JsonProperty("main")
    private Main main;

    @JsonProperty("wind")
    private Wind wind;

    @JsonProperty("clouds")
    private Clouds clouds;

    @JsonProperty("rain")
    private RainSnowFall rain;

    @JsonProperty("snow")
    private RainSnowFall snow;

    @JsonProperty("dt")
    private long dt;

    @JsonProperty("sys")
    private Sys sys;

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("cod")
    private int cod;

    @Data
    private static class Coord {
        private double lon;
        private double lat;
    }

    @Data
    private static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;
    }

    @Data
    private static class Main {
        private double temp;
        private int pressure;
        private int humidity;
        @JsonProperty("temp_min")
        private double tempMin;
        @JsonProperty("temp_max")
        private double tempMax;
        @JsonProperty("sea_level")
        private int seaLevel;
        @JsonProperty("grnd_level")
        private int grndLevel;
    }

    @Data
    private static class Wind {
        private float speed;
        private int deg;
    }

    @Data
    private static class Clouds {
        private int all;
    }

    @Data
    private static class RainSnowFall {
        @JsonProperty("1h")
        private int oneH;
        @JsonProperty("3h")
        private int threeH;
    }

    @Data
    private static class Sys {
        private int type;
        private int id;
        private String message;
        private String country;
        private long sunrise;
        private long sunset;
    }
}
