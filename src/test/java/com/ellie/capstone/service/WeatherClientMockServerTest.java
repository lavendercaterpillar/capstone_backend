package com.ellie.capstone.service;

import com.ellie.capstone.service.WeatherClient.GeoLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
class WeatherClientMockServerTest {

    private WeatherClient weatherClient;
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    @Value("${weather.api.key}")
    private String apiKey;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplateBuilder().build();
        weatherClient = new WeatherClient(restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testGetLatLon() {
        String city = "Seattle";
        String jsonResponse = "[{\"lat\":47.6062,\"lon\":-122.3321}]";

        mockServer.expect(once(), requestTo(org.hamcrest.Matchers.containsString("geo/1.0/direct")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        GeoLocation location = weatherClient.getLatLon(city);

        assertThat(location.getLat()).isEqualTo(47.6062);
        assertThat(location.getLon()).isEqualTo(-122.3321);

        mockServer.verify();
    }

    @Test
    void testGetCurrentWeather() {
        String city = "Seattle";

        // First call: geocoding
        String geoJson = "[{\"lat\":47.6062,\"lon\":-122.3321}]";
        mockServer.expect(once(), requestTo(org.hamcrest.Matchers.containsString("geo/1.0/direct")))
                .andRespond(withSuccess(geoJson, MediaType.APPLICATION_JSON));

        // Second call: current weather
        String weatherJson = "{ \"main\": { \"temp\": 300.15, \"humidity\": 65, \"pressure\": 1013 } }";
        mockServer.expect(once(), requestTo(org.hamcrest.Matchers.containsString("data/2.5/weather")))
                .andRespond(withSuccess(weatherJson, MediaType.APPLICATION_JSON));

        WeatherClient.CurrentWeather currentWeather = weatherClient.getCurrentWeather(city);

        assertThat(currentWeather.getDryBulbTempF()).isCloseTo(80.33, within(0.5));
        assertThat(currentWeather.getHumidity()).isEqualTo(65);
        assertThat(currentWeather.getPressure()).isEqualTo(1013);

        mockServer.verify();
    }

    @Test
    void testGetAverageTempSummer() {
        double avg = weatherClient.getAverageTemp(
                "Seattle",
                LocalDate.of(2023, 7, 1),
                LocalDate.of(2023, 7, 10)
        );
        assertThat(avg).isEqualTo(80.0);
    }

    @Test
    void testGetAverageTempWinter() {
        double avg = weatherClient.getAverageTemp(
                "Seattle",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 10)
        );
        assertThat(avg).isEqualTo(40.0);
    }
}