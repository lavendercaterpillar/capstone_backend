package com.ellie.capstone.service;

import com.ellie.capstone.model.Weather;
import com.ellie.capstone.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WeatherServiceTest {

    private WeatherClient weatherClient;
    private WeatherRepository weatherRepository;
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherClient = mock(WeatherClient.class);
        weatherRepository = mock(WeatherRepository.class);
        weatherService = new WeatherService(weatherClient, weatherRepository);
    }

    @Test
    void testGetWeatherSummary() {
        String city = "TestCity";

        // Mock current weather from client
        WeatherClient.CurrentWeather currentWeather = mock(WeatherClient.CurrentWeather.class);
        when(weatherClient.getCurrentWeather(city)).thenReturn(currentWeather);
        when(currentWeather.getDryBulbTempF()).thenReturn(80.0);
        when(currentWeather.getHumidity()).thenReturn(60);
        when(currentWeather.getPressure()).thenReturn(29.92);

        // Mock wet bulb calculation
        when(weatherClient.calculateWetBulbF(80.0, 60, 29.92)).thenReturn(70.0);

        // Mock average seasonal temps
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();

        when(weatherClient.getAverageTemp(eq(city), eq(LocalDate.of(currentYear, 6, 1)), eq(LocalDate.of(currentYear, 8, 31))))
                .thenReturn(85.0);

        when(weatherClient.getAverageTemp(eq(city), eq(LocalDate.of(currentYear - 1, 12, 1)), eq(LocalDate.of(currentYear, 2, 28))))
                .thenReturn(30.0);

        Map<String, Object> summary = weatherService.getWeatherSummary(city);

        assertThat(summary).isNotNull();
        assertThat(summary.get("dryBulbF")).isEqualTo(80.0);
        assertThat(summary.get("humidity")).isEqualTo(60);
        assertThat(summary.get("pressure")).isEqualTo(29.92);
        assertThat(summary.get("wetBulbF")).isEqualTo(70.0);
        assertThat(summary.get("avgSummerF")).isEqualTo(85.0);
        assertThat(summary.get("avgWinterF")).isEqualTo(30.0);
    }

    @Test
    void testFindByLocationExists() {
        String location = "TestLocation";
        Weather weather = new Weather(location, 90.0, 40.0, 65.0, 75.0);

        when(weatherRepository.findByLocationIgnoreCase(location)).thenReturn(Optional.of(weather));

        Optional<Weather> found = weatherService.findByLocation(location);

        assertThat(found).isPresent();
        assertThat(found.get().getLocation()).isEqualTo(location);
        assertThat(found.get().getAvgSummerTemp()).isEqualTo(90.0);
        assertThat(found.get().getAvgWinterTemp()).isEqualTo(40.0);
        assertThat(found.get().getWetBulbTemp()).isEqualTo(65.0);
        assertThat(found.get().getDryBulbTemp()).isEqualTo(75.0);
    }

    @Test
    void testFindByLocationNotExists() {
        String location = "UnknownLocation";

        when(weatherRepository.findByLocationIgnoreCase(location)).thenReturn(Optional.empty());

        Optional<Weather> found = weatherService.findByLocation(location);

        assertThat(found).isNotPresent();
    }

    @Test
    void testSaveWeather() {
        Weather weather = new Weather("SaveLocation", 95.0, 35.0, 68.0, 78.0);

        when(weatherRepository.save(weather)).thenReturn(weather);

        Weather saved = weatherService.save(weather);

        assertThat(saved).isNotNull();
        assertThat(saved.getLocation()).isEqualTo("SaveLocation");
        assertThat(saved.getAvgSummerTemp()).isEqualTo(95.0);
        assertThat(saved.getAvgWinterTemp()).isEqualTo(35.0);
        assertThat(saved.getWetBulbTemp()).isEqualTo(68.0);
        assertThat(saved.getDryBulbTemp()).isEqualTo(78.0);

        verify(weatherRepository, times(1)).save(weather);
    }
}