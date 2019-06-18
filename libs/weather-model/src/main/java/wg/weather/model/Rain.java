package wg.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rain {
    @JsonProperty("1h")
    private int oneHour;
    @JsonProperty("3h")
    private int threeHours;
}