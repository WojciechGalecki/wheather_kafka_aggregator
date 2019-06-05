package wg.weather.streams;

import java.util.Properties;

import javax.annotation.PostConstruct;

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

    public static final double ZERO_CELSIUS_IN_KELWIN = 273.15;
    public static final double THIRTY_CELSIUS_IN_KELWIN = 303.15;
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

        KStream<String, WeatherModel> weatherData = builder.stream(kafkaTopicsProperties.getWeather());

        KStream<String, WeatherModel>[] branches = weatherData.branch(
            (k, data) -> isTempBelowZeroCelsius(data),
            (k, data) -> isTempOverThirtyCelsius(data)
        );

        KStream<String, WeatherModel> tempBelowZero = branches[0];
        KStream<String, WeatherModel> tempOverThirty = branches[1];

        tempBelowZero.peek((k, data) -> log.info("Weather data from Poland: {}", data.getName()))
        .to(kafkaTopicsProperties.getLowTemp());
        tempOverThirty.peek((k, data) -> log.info("Weather data with high temp: {}", getTemp(data)))
        .to(kafkaTopicsProperties.getHighTemp());

        return new KafkaStreams(builder.build(), config);
    }

    private boolean isTempBelowZeroCelsius(WeatherModel data) {
        return getTemp(data) < ZERO_CELSIUS_IN_KELWIN;
    }

    private boolean isTempOverThirtyCelsius(WeatherModel data) {
        return getTemp(data) > THIRTY_CELSIUS_IN_KELWIN;
    }

    private Double getTemp(WeatherModel data) {
        return data.getMain().getTemp();
    }
}
