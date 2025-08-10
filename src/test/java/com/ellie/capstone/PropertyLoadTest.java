package com.ellie.capstone;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PropertyLoadTest {

    @Autowired
    private Environment env;

    @Test
    public void contextLoads() {
        // Basic sanity check to make sure Spring Boot context loads successfully
    }

    @Test
    public void testWeatherApiKeyIsLoaded() {
        String apiKey = env.getProperty("weather.api.key");
        assertThat(apiKey).isNotNull();
        assertThat(apiKey).isNotEmpty();
        System.out.println("Loaded weather.api.key = " + apiKey);
    }
}

