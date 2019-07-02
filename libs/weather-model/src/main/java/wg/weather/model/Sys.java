package wg.weather.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sys {
    private int type;
    private long id;
    private String message;
    private String country;
    private long sunrise;
    private long sunset;
}