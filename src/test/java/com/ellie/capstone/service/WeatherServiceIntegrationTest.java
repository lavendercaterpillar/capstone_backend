package com.ellie.capstone.service;

import com.ellie.capstone.model.Weather;
import com.ellie.capstone.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@SpringBootTest
class WeatherServiceIntegrationTest {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private RestTemplate restTemplate;

    @MockBean
    private WeatherRepository weatherRepository;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void getWeatherSummary_shouldReturnCompleteData() throws Exception {
        // Mock geocoding response
        String geoResponse = """
            [{"lat": 40.7128, "lon": -74.006}]
            """;

        // Mock current weather response
        String weatherResponse = """
            {
                "main": {
                    "temp": 290.15,
                    "humidity": 65,
                    "pressure": 1012
                }
            }
            """;

        // Setup mock server expectations
        mockServer.expect(requestTo(containsString("geo/1.0/direct")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(geoResponse, MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo(containsString("data/2.5/weather")))
                .andRespond(withSuccess(weatherResponse, MediaType.APPLICATION_JSON));

        // Mock repository behavior
        when(weatherRepository.findByLocationIgnoreCase("New York"))
                .thenReturn(Optional.empty());
        when(weatherRepository.save(any(Weather.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Execute test
        var summary = weatherService.getWeatherSummary("New York");

        // Verify results
        assertNotNull(summary);
        assertEquals(62.6, (Double)summary.get("dryBulbF"), 0.1);
        assertEquals(65, summary.get("humidity"));
        assertEquals(1012, summary.get("pressure"));
        assertNotNull(summary.get("wetBulbF"));
        assertEquals(80.0, summary.get("avgSummerF")); // From hardcoded values
        assertEquals(40.0, summary.get("avgWinterF")); // From hardcoded values

        // Verify interactions
        mockServer.verify();
        verify(weatherRepository).save(any(Weather.class));
    }

    @Test
    void findByLocation_shouldReturnExistingWeather() {
        Weather mockWeather = new Weather();
        mockWeather.setLocation("New York");
        mockWeather.setAvgSummerTemp(85.0);

        when(weatherRepository.findByLocationIgnoreCase("New York"))
                .thenReturn(Optional.of(mockWeather));

        Optional<Weather> result = weatherService.findByLocation("New York");

        assertTrue(result.isPresent());
        assertEquals("New York", result.get().getLocation());
        assertEquals(85.0, result.get().getAvgSummerTemp());
    }

    @Test
    void save_shouldPersistWeatherData() {
        Weather weatherToSave = new Weather();
        weatherToSave.setLocation("Chicago");

        Weather savedWeather = new Weather();
        savedWeather.setId(1L);
        savedWeather.setLocation("Chicago");

        when(weatherRepository.save(weatherToSave))
                .thenReturn(savedWeather);

        Weather result = weatherService.save(weatherToSave);

        assertEquals(1L, result.getId());
        assertEquals("Chicago", result.getLocation());
    }

    @Test
    void getWeatherSummary_shouldHandleApiErrors() {
        // Mock failed geocoding response
        mockServer.expect(requestTo(containsString("geo/1.0/direct")))
                .andRespond(withServerError());

        // Verify exception is thrown
        assertThrows(RuntimeException.class, () -> {
            weatherService.getWeatherSummary("Unknown City");
        });

        mockServer.verify();
    }
}