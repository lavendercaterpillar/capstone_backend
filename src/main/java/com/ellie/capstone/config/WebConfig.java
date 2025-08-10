package com.ellie.capstone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")  // Apply CORS to all /api/** endpoints
                        .allowedOriginPatterns(
                                "http://localhost:5173",             // Vite React dev server
                                "https://hvac-system-design.onrender.com" // Production frontend URL
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed HTTP methods
                        .allowedHeaders("*")             // Allow any headers
                        .allowCredentials(true);         // Enable sending cookies/auth headers
            }
        };
    }

    // Bean for RestTemplate to use in your services
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
