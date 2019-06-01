package wg.weather.model;

import lombok.Data;

@Data
public class City {

    private String country;
    private int geonameid;
    private String name;
    private String subcountry;
}
