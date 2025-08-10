package com.ellie.capstone.service;

import com.ellie.capstone.service.WeatherClient.CurrentWeatherResponse;
import com.ellie.capstone.service.WeatherClient.GeoLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherClient weatherClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Manually inject apiKey since @Value is not processed in plain unit tests
        TestUtils.setField(weatherClient, "apiKey", "fake-api-key");
    }

    @Test
    void getLatLon_success() {
        GeoLocation location = new GeoLocation();
        location.setLat(12.34);
        location.setLon(56.78);

        when(restTemplate.getForObject(anyString(), eq(GeoLocation[].class)))
                .thenReturn(new GeoLocation[]{location});

        GeoLocation result = weatherClient.getLatLon("London");

        assertEquals(12.34, result.getLat());
        assertEquals(56.78, result.getLon());
    }

    @Test
    void getLatLon_noResults_throws() {
        when(restTemplate.getForObject(anyString(), eq(GeoLocation[].class)))
                .thenReturn(new GeoLocation[]{});

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> weatherClient.getLatLon("UnknownCity"));

        assertTrue(ex.getMessage().contains("No location found"));
    }

    @Test
    void getAverageTemp_summerBranch() {
        double temp = weatherClient.getAverageTemp(
                "Miami", LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 10));
        assertEquals(80.0, temp);
    }

    @Test
    void getAverageTemp_winterBranch() {
        double temp = weatherClient.getAverageTemp(
                "Chicago", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 10));
        assertEquals(40.0, temp);
    }

    @Test
    void getCurrentWeather_success() {
        GeoLocation location = new GeoLocation();
        location.setLat(12.34);
        location.setLon(56.78);

        when(restTemplate.getForObject(contains("geo"), eq(GeoLocation[].class)))
                .thenReturn(new GeoLocation[]{location});

        CurrentWeatherResponse.Main main = new CurrentWeatherResponse.Main();
        main.setTemp(300.0); // Kelvin
        main.setHumidity(50);
        main.setPressure(1013);

        CurrentWeatherResponse response = new CurrentWeatherResponse();
        response.setMain(main);

        when(restTemplate.getForObject(contains("weather"), eq(CurrentWeatherResponse.class)))
                .thenReturn(response);

        WeatherClient.CurrentWeather result = weatherClient.getCurrentWeather("Paris");

        assertEquals(80.33, result.getDryBulbTempF(), 0.5);
        assertEquals(50, result.getHumidity());
        assertEquals(1013, result.getPressure());
    }

    @Test
    void getCurrentWeather_nullMain_throws() {
        GeoLocation location = new GeoLocation();
        location.setLat(12.34);
        location.setLon(56.78);

        when(restTemplate.getForObject(contains("geo"), eq(GeoLocation[].class)))
                .thenReturn(new GeoLocation[]{location});

        CurrentWeatherResponse response = new CurrentWeatherResponse();
        response.setMain(null);

        when(restTemplate.getForObject(contains("weather"), eq(CurrentWeatherResponse.class)))
                .thenReturn(response);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> weatherClient.getCurrentWeather("Paris"));
        assertTrue(ex.getMessage().contains("Failed to fetch current weather"));
    }

    @Test
    void calculateWetBulbF_returnsExpected() {
        double wetBulb = weatherClient.calculateWetBulbF(86.0, 60, 1013);
        // We just check it returns a plausible value
        assertTrue(wetBulb > 50 && wetBulb < 86);
    }

    // Utility to set private field without Spring
    static class TestUtils {
        static void setField(Object target, String fieldName, Object value) {
            try {
                java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

