package com.ellie.capstone.service;

import com.ellie.capstone.model.Weather;
import com.ellie.capstone.repository.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherClient weatherClient;

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void getWeatherSummary_shouldReturnCompleteData() {
        // Mock weather client responses
        WeatherClient.CurrentWeather mockCurrentWeather =
                new WeatherClient.CurrentWeather(75.0, 60, 1015);

        when(weatherClient.getCurrentWeather(anyString()))
                .thenReturn(mockCurrentWeather);
        when(weatherClient.calculateWetBulbF(75.0, 60, 1015))
                .thenReturn(68.5);
        when(weatherClient.getAverageTemp(anyString(), any(), any()))
                .thenReturn(80.0)  // Summer
                .thenReturn(40.0); // Winter

        Map<String, Object> summary = weatherService.getWeatherSummary("New York");

        assertNotNull(summary);
        assertEquals(75.0, summary.get("dryBulbF"));
        assertEquals(60, summary.get("humidity"));
        assertEquals(1015, summary.get("pressure"));
        assertEquals(68.5, summary.get("wetBulbF"));
        assertEquals(80.0, summary.get("avgSummerF"));
        assertEquals(40.0, summary.get("avgWinterF"));
    }

    @Test
    void findByLocation_shouldReturnWeatherIfExists() {
        Weather mockWeather = new Weather();
        mockWeather.setLocation("New York");
        when(weatherRepository.findByLocationIgnoreCase("New York"))
                .thenReturn(Optional.of(mockWeather));

        Optional<Weather> result = weatherService.findByLocation("New York");

        assertTrue(result.isPresent());
        assertEquals("New York", result.get().getLocation());
    }

    @Test
    void save_shouldPersistWeatherData() {
        Weather weatherToSave = new Weather();
        weatherToSave.setLocation("New York");

        Weather savedWeather = new Weather();
        savedWeather.setId(1L);
        savedWeather.setLocation("New York");

        when(weatherRepository.save(any(Weather.class)))
                .thenReturn(savedWeather);

        Weather result = weatherService.save(weatherToSave);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(weatherRepository).save(weatherToSave);
    }
}