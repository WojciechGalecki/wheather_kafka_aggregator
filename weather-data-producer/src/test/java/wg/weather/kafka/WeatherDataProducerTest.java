package wg.weather.kafka;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.SettableListenableFuture;

import wg.weather.client.WeatherClient;
import wg.weather.loader.CityNamesLoader;
import wg.weather.mapper.WeatherDataMapper;
import wg.weather.properties.KafkaTopicsProperties;

@RunWith(MockitoJUnitRunner.class)
public class WeatherDataProducerTest {
    private static final String TOPIC = "topic";
    private static final String CITY = "city";

    @Mock
    WeatherClient weatherClient;
    @Mock
    CityNamesLoader cityNamesLoader;
    @Mock
    KafkaTemplate kafkaTemplate;
    @Mock
    KafkaTopicsProperties kafkaTopicsProperties;
    @InjectMocks
    WeatherDataProducer weatherDataProducer;

    @Test
    public void When_SendWeatherData_Expect_SendingDataToProperKafkaTopic() throws IOException {
        //given
        var weatherData = Optional.of(wg.weather.model.WeatherData.builder()
            .coord(new wg.weather.model.Coord(1.0, 1.0))
            .weather(Collections.singletonList(wg.weather.model.Weather.builder()
                .id(1L)
                .description("")
                .main("")
                .icon("")
                .build()))
            .base("")
            .main(wg.weather.model.Main.builder()
                .grndLevel(1L)
                .humidity(1L)
                .pressure(1L)
                .seaLevel(1L)
                .temp(1.0)
                .tempMax(1.0)
                .tempMin(0.0)
                .build())
            .wind(new wg.weather.model.Wind(1.0, 1L))
            .clouds(new wg.weather.model.Clouds(1L))
            .dt(1L)
            .sys(wg.weather.model.Sys.builder()
                .country("")
                .id(1L)
                .message("")
                .sunrise(1L)
                .sunset(1L)
                .type(1)
                .build())
            .id(1L)
            .name("")
            .cod(1L).build());

        var weatherDataAvro = WeatherDataMapper.mapToAvro(weatherData.get());

        //when
        when(kafkaTopicsProperties.getWeather()).thenReturn(TOPIC);
        when(cityNamesLoader.getCityNames()).thenReturn(Collections.singletonList(CITY));
        when(weatherClient.getWeatherByCityName(CITY)).thenReturn(weatherData);

        SettableListenableFuture<SendResult<Object, Object>> kafkaFuture = new SettableListenableFuture<>();
        kafkaFuture.set(new SendResult<>(null, null));
        when(kafkaTemplate.send(any(), any(), any())).thenReturn(kafkaFuture);

        //then
        weatherDataProducer.sendWeatherData();

        verify(kafkaTemplate).send(TOPIC, CITY, weatherDataAvro);
    }
}