package wg.weather.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import wg.weather.model.Coord;
import wg.weather.model.Rain;
import wg.weather.model.Weather;
import wg.weather.model.WeatherData;

public class WeatherDataMapperTest {

    @Test
    public void When_MapToAvro_Expect_ProperAvro() {
        //given
        WeatherData weatherData = WeatherData.builder()
            .base("base")
            .coord(new Coord(1.0, 1.0))
            .weather(Collections.singletonList(
                new Weather(1L, "main", "desc", null)
            ))
            .rain(new Rain(1, 1))
            .build();

        //when and then
        var avroWeatherData = WeatherDataMapper.mapToAvro(weatherData);

        assertThat(avroWeatherData.getBase()).isEqualTo(weatherData.getBase());
        assertThat(avroWeatherData.getCoord().getLat()).isEqualTo(weatherData.getCoord().getLat());
        assertThat(avroWeatherData.getCoord().getLon()).isEqualTo(weatherData.getCoord().getLon());
    }
}