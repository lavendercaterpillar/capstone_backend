package com.ellie.capstone.repository;

import com.ellie.capstone.model.Weather;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class WeatherRepositoryTest {

    @Autowired
    private WeatherRepository weatherRepository;

    @Test
    @DisplayName("Should find weather by location ignoring case")
    void findByLocationIgnoreCase_shouldReturnWeather() {
        // Arrange
        Weather weather = new Weather("Seattle", 25.0, 5.0, 20.0, 22.0);
        weatherRepository.save(weather);

        // Act
        Optional<Weather> result = weatherRepository.findByLocationIgnoreCase("seAttLe");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getAvgSummerTemp()).isEqualTo(25.0);
    }

    @Test
    @DisplayName("Should return empty if location does not exist")
    void findByLocationIgnoreCase_shouldReturnEmpty() {
        Optional<Weather> result = weatherRepository.findByLocationIgnoreCase("Atlantis");
        assertThat(result).isEmpty();
    }
}
