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
import wg.weather.model.Clouds;
import wg.weather.model.Coord;
import wg.weather.model.Main;
import wg.weather.model.Sys;
import wg.weather.model.WeatherItem;
import wg.weather.model.WeatherModel;
import wg.weather.model.Wind;
import wg.weather.properties.KafkaTopicsProperties;
import wg.weather.loader.CityNamesLoader;

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
    public void When_() throws IOException {
        //given
        var weatherModel = Optional.of(WeatherModel.newBuilder()
            .setCoord(new Coord(1.0, 1.0))
            .setWeather(Collections.singletonList(WeatherItem.newBuilder()
                .setId(1)
                .setDescription("")
                .setMain("")
                .setIcon("")
                .build()))
            .setBase("")
            .setMain(Main.newBuilder()
                .setGrndLevel(1)
                .setHumidity(1)
                .setPressure(1)
                .setSeaLevel(1)
                .setTemp(1.0)
                .setTempMax(1.0)
                .setTempMin(1.0)
                .build())
            .setWind(new Wind(1.0f, 1))
            .setClouds(new Clouds(1))
            .setDt(1L)
            .setSys(Sys.newBuilder()
                .setCountry("")
                .setId(1)
                .setMessage("")
                .setSunrise(1L)
                .setSunset(1L)
                .setType(1)
                .build())
            .setId(1)
            .setName("")
            .setCod(1)
            .build());

        //when
        when(kafkaTopicsProperties.getWeather()).thenReturn(TOPIC);
        when(cityNamesLoader.getCityNames()).thenReturn(Collections.singletonList(CITY));
        when(weatherClient.getWeatherByCityName(CITY)).thenReturn(weatherModel);

        SettableListenableFuture<SendResult<Object, Object>> kafkaFuture = new SettableListenableFuture<>();
        kafkaFuture.set(new SendResult<>(null, null));
        when(kafkaTemplate.send(any(), any(), any())).thenReturn(kafkaFuture);

        //then
        weatherDataProducer.sendWeatherData();

        verify(kafkaTemplate).send(TOPIC, CITY, weatherModel.get());
    }
}