package com.ellie.capstone.service;

import com.ellie.capstone.model.Weather;
import com.ellie.capstone.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WeatherService {

    private final WeatherClient weatherClient;
    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherService(WeatherClient weatherClient, WeatherRepository weatherRepository) {
        this.weatherClient = weatherClient;
        this.weatherRepository = weatherRepository;
    }

    public Map<String, Object> getWeatherSummary(String city) {
        Map<String, Object> weatherSummary = new HashMap<>();

        // Current conditions
        WeatherClient.CurrentWeather current = weatherClient.getCurrentWeather(city);
        double wetBulb = weatherClient.calculateWetBulbF(
                current.getDryBulbTempF(),
                current.getHumidity(),
                current.getPressure()
        );

        weatherSummary.put("dryBulbF", current.getDryBulbTempF());
        weatherSummary.put("humidity", current.getHumidity());
        weatherSummary.put("pressure", current.getPressure());
        weatherSummary.put("wetBulbF", wetBulb);

        // Seasonal averages
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();

        weatherSummary.put("avgSummerF", weatherClient.getAverageTemp(
                city,
                LocalDate.of(currentYear, 6, 1),
                LocalDate.of(currentYear, 8, 31)
        ));

        // Winter spans Dec previous year to Feb current year
        weatherSummary.put("avgWinterF", weatherClient.getAverageTemp(
                city,
                LocalDate.of(currentYear - 1, 12, 1),
                LocalDate.of(currentYear, 2, 28)
        ));

        return weatherSummary;
    }

    public Optional<Weather> findByLocation(String location) {
        return weatherRepository.findByLocationIgnoreCase(location);
    }

    public Weather save(Weather weather) {
        return weatherRepository.save(weather);
    }
}
