package com.ellie.capstone.service;

import com.ellie.capstone.exception.WeatherDataException;
import com.ellie.capstone.model.Weather;
import com.ellie.capstone.repository.WeatherRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final WeatherClient weatherClient;

    public WeatherService(WeatherRepository weatherRepository, WeatherClient weatherClient) {
        this.weatherRepository = weatherRepository;
        this.weatherClient = weatherClient;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "weather", key = "#location.toLowerCase()")
    public Optional<Weather> findByLocation(String location) {
        return weatherRepository.findByLocationIgnoreCase(location);
    }

    @Transactional
    @Retryable(maxAttempts = 3)
    public Weather getWeatherForLocation(String location) {
        return findByLocation(location)
                .orElseGet(() -> fetchAndSaveWeatherData(location));
    }

    @Transactional
    public Weather save(Weather weather) {
        return weatherRepository.save(weather);
    }

    public Map<String, Object> getWeatherSummary(String location) {
        WeatherClient.CurrentWeather current = weatherClient.getCurrentWeather(location);
        double wetBulb = weatherClient.calculateWetBulbF(
                current.getDryBulbTempF(),
                current.getHumidity(),
                current.getPressure()
        );

        Map<String, Object> summary = new HashMap<>();
        summary.put("dryBulbF", current.getDryBulbTempF());
        summary.put("wetBulbF", wetBulb);

        // Get seasonal averages
        LocalDate now = LocalDate.now();
        summary.put("avgSummerF", getSeasonalAverage(location, "summer"));
        summary.put("avgWinterF", getSeasonalAverage(location, "winter"));

        return summary;
    }

    private Weather fetchAndSaveWeatherData(String location) {
        Map<String, Object> weatherData = getWeatherSummary(location);

        Weather weather = new Weather();
        weather.setLocation(location);
        weather.setDryBulbTemp((Double) weatherData.get("dryBulbF"));
        weather.setWetBulbTemp((Double) weatherData.get("wetBulbF"));
        weather.setAvgSummerTemp((Double) weatherData.get("avgSummerF"));
        weather.setAvgWinterTemp((Double) weatherData.get("avgWinterF"));

        return save(weather);
    }

    private double getSeasonalAverage(String location, String season) {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();

        if ("summer".equalsIgnoreCase(season)) {
            return weatherClient.getAverageTemp(
                    location,
                    LocalDate.of(currentYear, 6, 1),
                    LocalDate.of(currentYear, 8, 31)
            );
        } else {
            return weatherClient.getAverageTemp(
                    location,
                    LocalDate.of(currentYear - 1, 12, 1),
                    LocalDate.of(currentYear, 2, 28)
            );
        }
    }

    // Fallback method for circuit breaker
    @Transactional(readOnly = true)
    protected Weather getCachedWeather(String location, Exception ex) throws WeatherDataException {
        return findByLocation(location)
                .orElseThrow(() -> new WeatherDataException(
                        "Weather service unavailable and no cached data for: " + location
                ));
    }
}