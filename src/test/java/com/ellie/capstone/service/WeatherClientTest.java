package com.ellie.capstone.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@SpringBootTest
class WeatherClientTest {

    @Autowired
    private WeatherClient weatherClient;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void getLatLon_shouldReturnLocation() {
        String city = "New York";
        String mockResponse = """
            [{
                "lat": 40.7128,
                "lon": -74.006
            }]
            """;

        mockServer.expect(requestTo(
                        "http://api.openweathermap.org/geo/1.0/direct?q=New%20York&limit=1&appid=d5404a88664a7b6d8442f0d5b13f2d82"
                ))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        WeatherClient.GeoLocation location = weatherClient.getLatLon(city);

        assertEquals(40.7128, location.getLat(), 0.0001);
        assertEquals(-74.006, location.getLon(), 0.0001);
        mockServer.verify();
    }

    @Test
    void getCurrentWeather_shouldReturnWeatherData() {
        String city = "New York";
        String geoMockResponse = """
            [{
                "lat": 40.7128,
                "lon": -74.006
            }]
            """;

        String weatherMockResponse = """
            {
                "main": {
                    "temp": 290.15,
                    "humidity": 65,
                    "pressure": 1012
                }
            }
            """;

        // First request - geocoding
        mockServer.expect(requestTo(
                        "http://api.openweathermap.org/geo/1.0/direct?q=New%20York&limit=1&appid=d5404a88664a7b6d8442f0d5b13f2d82"
                ))
                .andRespond(withSuccess(geoMockResponse, MediaType.APPLICATION_JSON));

        // Second request - weather data
        mockServer.expect(requestTo(
                        "https://api.openweathermap.org/data/2.5/weather?lat=40.7128&lon=-74.006&appid=d5404a88664a7b6d8442f0d5b13f2d82"
                ))
                .andRespond(withSuccess(weatherMockResponse, MediaType.APPLICATION_JSON));

        WeatherClient.CurrentWeather currentWeather = weatherClient.getCurrentWeather(city);

        assertEquals(62.6, currentWeather.getDryBulbTempF(), 0.1);
        assertEquals(65, currentWeather.getHumidity());
        assertEquals(1012, currentWeather.getPressure());
        mockServer.verify();
    }

    @Test
    void getAverageTemp_shouldReturnHardcodedValues() {
        double summerTemp = weatherClient.getAverageTemp("New York",
                LocalDate.of(2023, 6, 1),
                LocalDate.of(2023, 8, 31));
        assertEquals(80.0, summerTemp);

        double winterTemp = weatherClient.getAverageTemp("New York",
                LocalDate.of(2022, 12, 1),
                LocalDate.of(2023, 2, 28));
        assertEquals(40.0, winterTemp);
    }

    @Test
    void kelvinToFahrenheit_shouldConvertCorrectly() {
        assertEquals(32.0, weatherClient.kelvinToFahrenheit(273.15), 0.01);  // Freezing
        assertEquals(212.0, weatherClient.kelvinToFahrenheit(373.15), 0.01); // Boiling
        assertEquals(-459.67, weatherClient.kelvinToFahrenheit(0), 0.01);    // Absolute zero
    }
}