package wg.weather.client;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import wg.weather.model.WeatherModel;
import wg.weather.properties.ApiProperties;

@Component
@Slf4j
public class WeatherClient {

    private ApiProperties apiProperties;
    private RestTemplate restTemplate;

    public WeatherClient(ApiProperties apiProperties, RestTemplate restTemplate) {
        this.apiProperties = apiProperties;
        this.restTemplate = restTemplate;
    }

    public WeatherModel getWeatherByCityName(String cityName) {
        URI uri = UriComponentsBuilder
            .fromUriString(apiProperties.getUrl())
            .queryParam("q", cityName)
            .queryParam("appid", apiProperties.getId())
            .build()
            .toUri();

        return restTemplate.getForObject(uri, WeatherModel.class);
    }
}
