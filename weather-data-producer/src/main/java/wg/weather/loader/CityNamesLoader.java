package wg.weather.loader;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import wg.weather.model.City;

@Component
@AllArgsConstructor
public class CityNamesLoader {
    private static final String CITIES_PATH = "/cities/world-cities.json";

    private ResourceLoader resourceLoader;

    private ObjectMapper objectMapper;

    public List<String> getCityNames() throws IOException {
        URL resource = resourceLoader.getClass().getResource(CITIES_PATH);

        City[] cities = objectMapper.readValue(resource.openStream(), City[].class);

        return Arrays.stream(cities).map(City::getName).collect(Collectors.toList());
    }
}
