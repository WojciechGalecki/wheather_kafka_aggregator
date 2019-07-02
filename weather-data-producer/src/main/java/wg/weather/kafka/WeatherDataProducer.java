package wg.weather.kafka;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import wg.weather.avro.WeatherData;
import wg.weather.client.WeatherClient;
import wg.weather.mapper.WeatherDataMapper;
import wg.weather.properties.KafkaTopicsProperties;
import wg.weather.loader.CityNamesLoader;

@Slf4j
@Component
@AllArgsConstructor
public class WeatherDataProducer {

    private WeatherClient weatherClient;

    private CityNamesLoader cityNamesLoader;

    private KafkaTemplate kafkaTemplate;

    private KafkaTopicsProperties kafkaTopicsProperties;

    @PostConstruct
    public void sendWeatherData() throws IOException {
        var cityNames = cityNamesLoader.getCityNames();

        cityNames.forEach(name -> {
            try {
                sendWeatherDataByCityName(name);
            } catch (Throwable ex) {
                log.error("Unexpected error during sending data towards kafka: \n {}", ex.getMessage());
            }
        });
    }

    private void sendWeatherDataByCityName(String name) throws Throwable {
        var optionalWeatherData = weatherClient.getWeatherByCityName(name);

        if (optionalWeatherData.isPresent()) {

            wg.weather.avro.WeatherData weatherDataAvro = WeatherDataMapper.mapToAvro(optionalWeatherData.get());

            ListenableFuture<SendResult<String, wg.weather.avro.WeatherData>> result
                = kafkaTemplate.send(kafkaTopicsProperties.getWeather(), name, weatherDataAvro);

            result.addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onSuccess(SendResult<String, WeatherData> result) {
                    var metadata = result.getRecordMetadata();
                    log.info("Successfully send data for: {} on topic: {} and partition: {}", name,
                        metadata.topic(), metadata.partition());
                }

                @Override
                public void onFailure(Throwable ex) {
                    log.error("Unable to send data for city: {} due to: {}", name, ex.getMessage());
                }
            });
        }
    }
}