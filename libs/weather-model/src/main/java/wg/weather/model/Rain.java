package wg.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rain {
    @JsonProperty("1h")
    private int oneHour;
    @JsonProperty("3h")
    private int threeHours;
}