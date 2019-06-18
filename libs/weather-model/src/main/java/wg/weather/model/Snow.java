package wg.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Snow {
    @JsonProperty("1h")
    private int oneHour;
    @JsonProperty("3h")
    private int threeHours;
}