package com.ellie.capstone.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class WeatherTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailValidationWhenAvgSummerTempNull() {
        Weather weather = new Weather();
        weather.setLocation("Seattle");
        weather.setAvgSummerTemp(null);

        Set<ConstraintViolation<Weather>> violations = validator.validate(weather);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("avgSummerTemp"));
    }

    @Test
    void gettersAndSettersShouldWork() {
        Weather weather = new Weather();
        weather.setLocation("TestCity");
        assertThat(weather.getLocation()).isEqualTo("TestCity");
    }

    @Test
    void testAllAttributes() {
        Weather weather = new Weather();

        weather.setId(1L);
        weather.setLocation("Chicago");
        weather.setAvgSummerTemp(28.5);
        weather.setAvgWinterTemp(-10.0);
        weather.setWetBulbTemp(22.5);
        weather.setDryBulbTemp(18.0);

        assertEquals(1L, weather.getId());
        assertEquals("Chicago", weather.getLocation());
        assertEquals(28.5, weather.getAvgSummerTemp());
        assertEquals(-10.0, weather.getAvgWinterTemp());
        assertEquals(22.5, weather.getWetBulbTemp());
        assertEquals(18.0, weather.getDryBulbTemp());
    }

    @Test
    void testManyToOneRelationshipWithProjects() {
        Weather weather = new Weather(
                "Miami",
                32.0,
                18.0,
                27.0,
                25.0
        );

        Project project1 = new Project();
        project1.setProjectName("Beach House");
        project1.setLocation("Miami");
        project1.setWeather(weather);

        Project project2 = new Project();
        project2.setProjectName("Ocean View Condo");
        project2.setLocation("Miami");
        project2.setWeather(weather);

        assertSame(weather, project1.getWeather());
        assertSame(weather, project2.getWeather());
        assertEquals("Miami", project1.getWeather().getLocation());
        assertEquals("Miami", project2.getWeather().getLocation());
    }
}

