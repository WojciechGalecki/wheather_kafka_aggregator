package wg.weather.client;

import java.net.URI;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import wg.weather.model.WeatherData;
import wg.weather.properties.ApiProperties;

@Component
@Slf4j
@AllArgsConstructor
public class WeatherClient {
    private static final String Q_PARAM = "q";
    private static final String APPID_PARAM = "APPID";

    private ApiProperties apiProperties;
    private RestTemplate restTemplate;

    public Optional<WeatherData> getWeatherByCityName(String cityName) {
        log.info("Getting weather data for city: {}", cityName);

        URI uri = getApiUri(cityName);

        WeatherData model;

        try {
            model = restTemplate.getForObject(uri, WeatherData.class);
        } catch (Exception e) {
            log.error("Failed to load data for city: {}", cityName);

            return Optional.empty();
        }
        log.info("Successfully get weather data for city: {}", cityName);

        return Optional.ofNullable(model);
    }

    private URI getApiUri(String cityName) {
        return UriComponentsBuilder
            .fromUriString(apiProperties.getUrl())
            .queryParam(Q_PARAM, cityName)
            .queryParam(APPID_PARAM, apiProperties.getId())
            .build()
            .toUri();
    }
}
