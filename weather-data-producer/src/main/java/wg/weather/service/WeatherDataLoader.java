package wg.weather.service;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import wg.weather.client.WeatherClient;
import wg.weather.utils.CityNamesLoader;

@Slf4j
@Component
@AllArgsConstructor
public class WeatherDataLoader {

    private WeatherClient weatherClient;

    private CityNamesLoader cityNamesLoader;

    @PostConstruct
    private void loadWeatherData() throws IOException {
        var cityNames = cityNamesLoader.getCityNames();

        cityNames.forEach(this::getWeatherByCityName);
    }

    private void getWeatherByCityName(String name) {
        var optionalWeatherModel = weatherClient.getWeatherByCityName(name);

        optionalWeatherModel.ifPresent(model -> log.info(model.toString()));
    }
}