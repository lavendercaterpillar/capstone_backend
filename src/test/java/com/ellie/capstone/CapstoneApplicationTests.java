package com.ellie.capstone;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CapstoneApplicationTests {

    @Value("${weather.api.key}")
    private String apiKey;

    @Test
    public void testApiKeyLoaded() {
        assertNotNull(apiKey, "API key should be loaded from properties");
        System.out.println("Loaded API key: " + apiKey);
    }
}
