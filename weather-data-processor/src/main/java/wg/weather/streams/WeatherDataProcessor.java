package wg.weather.streams;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import wg.weather.model.WeatherModel;
import wg.weather.properties.KafkaStreamsProperties;
import wg.weather.properties.KafkaTopicsProperties;

@Slf4j
@Component
public class WeatherDataProcessor {

    @Autowired
    private KafkaTopicsProperties kafkaTopicsProperties;

    @Autowired
    private KafkaStreamsProperties kafkaStreamsProperties;

    @PostConstruct
    public void processData() {
        log.info("Starting process weather data...");
        KafkaStreams kafkaStreams = createTopology(kafkaStreamsProperties.getKafkaStreamsConfig());

        kafkaStreams.cleanUp();
        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }

    private KafkaStreams createTopology(Properties config) {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<Bytes, WeatherModel> weatherData = builder.stream(kafkaTopicsProperties.getWeather());

        KStream<Bytes, WeatherModel>[] branches = weatherData.branch(
            (k, data) -> isDataFromPoland(data),
            (k, data) -> isTempHigh(data)
        );

        KStream<Bytes, WeatherModel> dataFromPoland = branches[0];
        KStream<Bytes, WeatherModel> dataWithHighTemp = branches[1];

        dataFromPoland.peek((k, data) -> log.info("Weather data from Poland: {}", data.getName()))
        .to(kafkaTopicsProperties.getFromPoland());
        dataWithHighTemp.peek((k, data) -> log.info("Weather data with high temp: {}", data.getMain().getTemp()))
        .to(kafkaTopicsProperties.getHighTemp());

        return new KafkaStreams(builder.build(), config);
    }

    private boolean isDataFromPoland(WeatherModel data) {
        return data.getSys().getCountry().equals("Poland");
    }

    private boolean isTempHigh(WeatherModel data) {
        return data.getMain().getTemp() >= 30.0;
    }
}
