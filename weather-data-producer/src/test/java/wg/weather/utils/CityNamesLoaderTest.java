package wg.weather.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CityNamesLoaderTest {
    private static final int EXPECTED_CITY_NAMES_SIZE = 23018;

    private final ResourceLoader resourceLoader = new ResourceLoader() {
        @Override
        public Resource getResource(String location) {
            return null;
        }

        @Override
        public ClassLoader getClassLoader() {
            return null;
        }
    };

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final CityNamesLoader cityNamesLoader = new CityNamesLoader(resourceLoader, objectMapper);

    @Test
    public void When_GetCityNames_Expect_ListOfCityNames() throws IOException {
        List<String> cityNames = cityNamesLoader.getCityNames();

        assertThat(cityNames.size()).isEqualTo(EXPECTED_CITY_NAMES_SIZE);
    }
}