package wg.weather.mapper;

import java.util.ArrayList;
import java.util.List;

import wg.weather.avro.Clouds;
import wg.weather.avro.Coord;
import wg.weather.avro.Main;
import wg.weather.avro.Rain;
import wg.weather.avro.Snow;
import wg.weather.avro.Sys;
import wg.weather.avro.Weather;
import wg.weather.avro.WeatherData;
import wg.weather.avro.Wind;

public final class WeatherDataMapper {

    public static WeatherData mapToAvro(wg.weather.model.WeatherData data) {
        if (data == null) {
            return null;
        }

        return WeatherData.newBuilder()
            .setCoord(getAvroCoord(data.getCoord()))
            .setWeather(getAvroWeathers(data.getWeather()))
            .setBase(data.getBase())
            .setMain(getAvroMain(data.getMain()))
            .setWind(getAvroWind(data.getWind()))
            .setClouds(getAvroClouds(data.getClouds()))
            .setRain(getAvroRain(data.getRain()))
            .setSnow(getAvroSnow(data.getSnow()))
            .setDt(data.getDt())
            .setSys(getAvroSys(data.getSys()))
            .setId(data.getId())
            .setName(data.getName())
            .setCod(data.getCod())
            .build();
    }

    private static Coord getAvroCoord(wg.weather.model.Coord coord) {
        if (coord == null) {
            return null;
        }

        return Coord.newBuilder()
            .setLon(coord.getLon())
            .setLat(coord.getLat())
            .build();
    }

    private static List<Weather> getAvroWeathers(List<wg.weather.model.Weather> weather) {
        if (weather == null) {
            return null;
        }

        List<Weather> weatherAvroList = new ArrayList<>();

        weather.forEach(w -> weatherAvroList.add(Weather.newBuilder()
            .setId(w.getId())
            .setMain(w.getMain())
            .setDescription(w.getDescription())
            .setIcon(w.getIcon())
            .build()));
        return weatherAvroList;
    }

    private static Main getAvroMain(wg.weather.model.Main main) {
        if (main == null) {
            return null;
        }

        return Main.newBuilder()
            .setTemp(main.getTemp())
            .setPressure(main.getPressure())
            .setHumidity(main.getHumidity())
            .setTempMin(main.getTempMin())
            .setTempMax(main.getTempMax())
            .setSeaLevel(main.getSeaLevel())
            .setGrndLevel(main.getGrndLevel())
            .build();
    }

    private static Wind getAvroWind(wg.weather.model.Wind wind) {
        if (wind == null) {
            return null;
        }

        return Wind.newBuilder()
            .setSpeed(wind.getSpeed())
            .setDeg(wind.getDeg())
            .build();
    }

    private static Clouds getAvroClouds(wg.weather.model.Clouds clouds) {
        if (clouds == null) {
            return null;
        }

        return Clouds.newBuilder()
            .setAll(clouds.getAll())
            .build();
    }

    private static Rain getAvroRain(wg.weather.model.Rain rain) {
        if (rain == null) {
            return null;
        }

        return Rain.newBuilder()
            .setOneHour(rain.getOneHour())
            .setThreeHours(rain.getThreeHours())
            .build();
    }

    private static Snow getAvroSnow(wg.weather.model.Snow snow) {
        if (snow == null) {
            return null;
        }

        return Snow.newBuilder()
            .setOneHour(snow.getOneHour())
            .setThreeHours(snow.getThreeHours())
            .build();
    }

    private static Sys getAvroSys(wg.weather.model.Sys sys) {
        if (sys == null) {
            return null;
        }

        return Sys.newBuilder()
            .setType(sys.getType())
            .setId(sys.getId())
            .setMessage(sys.getMessage())
            .setCountry(sys.getCountry())
            .setSunrise(sys.getSunrise())
            .setSunset(sys.getSunset())
            .build();
    }
}
