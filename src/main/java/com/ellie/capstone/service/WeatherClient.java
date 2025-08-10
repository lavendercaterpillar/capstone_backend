package com.ellie.capstone.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@Service
public class WeatherClient {
    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Geocoding API: get lat/lon by city
    public GeoLocation getLatLon(String city) {
        String url = UriComponentsBuilder.fromHttpUrl("http://api.openweathermap.org/geo/1.0/direct")
                .queryParam("q", city)
                .queryParam("limit", 1)
                .queryParam("appid", apiKey)
                .build(false)  // Prevents double encoding
                .toUriString();

        GeoLocation[] locations = restTemplate.getForObject(url, GeoLocation[].class);

        if (locations == null || locations.length == 0) {
            throw new RuntimeException("No location found for city: " + city);
        }
        return locations[0];
    }

    // Temporary hardcoded average temps
    public double getAverageTemp(String city, LocalDate startDate, LocalDate endDate) {
        // For now, return hardcoded summer/winter averages
        // June-August = summer
        int startMonth = startDate.getMonthValue();
        if (startMonth >= 6 && startMonth <= 8) {
            return 80.0; // Avg summer temp in F
        } else {
            return 40.0; // Avg winter temp in F
        }
    }

    // === Step 1: Fetch current weather data from OpenWeatherMap ===
    public CurrentWeather getCurrentWeather(String city) {
        GeoLocation loc = getLatLon(city);

        String url = UriComponentsBuilder.fromHttpUrl("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat", loc.getLat())
                .queryParam("lon", loc.getLon())
                .queryParam("appid", apiKey)
                .build(false)  // Prevents double encoding
                .toUriString();

        CurrentWeatherResponse response = restTemplate.getForObject(url, CurrentWeatherResponse.class);

        if (response == null || response.getMain() == null) {
            throw new RuntimeException("Failed to fetch current weather for city: " + city);
        }

        double tempF = kelvinToFahrenheit(response.getMain().getTemp());
        int humidity = response.getMain().getHumidity();
        int pressure = response.getMain().getPressure();

        return new CurrentWeather(tempF, humidity, pressure);
    }

    // === Step 2: Wet bulb temperature calculation ===
    public double calculateWetBulbF(double dryBulbF, int humidityPercent, int pressureHPa) {
        double dryBulbC = (dryBulbF - 32) * 5 / 9; // Convert to Celsius for formula

        double rh = humidityPercent / 100.0;

        // Magnus formula for saturation vapor pressure
        double es = 6.112 * Math.exp((17.62 * dryBulbC) / (243.12 + dryBulbC));

        // Actual vapor pressure
        double e = rh * es;

        // Approximate wet bulb temperature formula in Celsius
        double wetBulbC = dryBulbC * Math.atan(0.151977 * Math.sqrt(rh + 8.313659))
                + Math.atan(dryBulbC + rh)
                - Math.atan(rh - 1.676331)
                + 0.00391838 * Math.pow(rh, 1.5) * Math.atan(0.023101 * rh)
                - 4.686035;

        // Convert back to Fahrenheit
        return wetBulbC * 9 / 5 + 32;
    }

    // Kelvin to Fahrenheit conversion
    // Change from private to package-private (no modifier)
    double kelvinToFahrenheit(double kelvin) {
        return (kelvin - 273.15) * 9/5 + 32;
    }
//    private double kelvinToFahrenheit(double kelvin) {
//        return (kelvin - 273.15) * 9/5 + 32;
//    }

    // === DTO classes ===

    public static class GeoLocation {
        private double lat;
        private double lon;

        public double getLat() { return lat; }
        public void setLat(double lat) { this.lat = lat; }

        public double getLon() { return lon; }
        public void setLon(double lon) { this.lon = lon; }
    }

    public static class TimeMachineResponse {
        private List<HourlyData> data;

        public List<HourlyData> getData() { return data; }
        public void setData(List<HourlyData> data) { this.data = data; }
    }

    public static class HourlyData {
        private long dt;
        private double temp;

        public long getDt() { return dt; }
        public void setDt(long dt) { this.dt = dt; }

        public double getTemp() { return temp; }
        public void setTemp(double temp) { this.temp = temp; }
    }

    public static class CurrentWeatherResponse {
        private Main main;

        public Main getMain() { return main; }
        public void setMain(Main main) { this.main = main; }

        public static class Main {
            private double temp;
            private int humidity;
            private int pressure;

            public double getTemp() { return temp; }
            public void setTemp(double temp) { this.temp = temp; }

            public int getHumidity() { return humidity; }
            public void setHumidity(int humidity) { this.humidity = humidity; }

            public int getPressure() { return pressure; }
            public void setPressure(int pressure) { this.pressure = pressure; }
        }
    }

    public static class CurrentWeather {
        private double dryBulbTempF;  // Fahrenheit
        private int humidity;         // %
        private int pressure;         // hPa

        public CurrentWeather(double dryBulbTempF, int humidity, int pressure) {
            this.dryBulbTempF = dryBulbTempF;
            this.humidity = humidity;
            this.pressure = pressure;
        }

        public double getDryBulbTempF() { return dryBulbTempF; }
        public int getHumidity() { return humidity; }
        public int getPressure() { return pressure; }
    }
}
