package wg.weather.client;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import wg.weather.model.WeatherModel;
import wg.weather.properties.ApiProperties;

@RunWith(MockitoJUnitRunner.class)
public class WeatherClientTest {

    @Mock
    ApiProperties properties;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    WeatherClient client;

    @Before
    public void setUp() {
        when(properties.getId()).thenReturn("id");
        when(properties.getUrl()).thenReturn("url");
    }

    @Test
    public void When_GetWeatherByCityName_Expect_ProperCallToApi() throws URISyntaxException {
        String expectedUrl = "url?q=city&appid=id";
        client.getWeatherByCityName("city");
        verify(restTemplate).getForObject(new URI(expectedUrl), WeatherModel.class);
    }
}