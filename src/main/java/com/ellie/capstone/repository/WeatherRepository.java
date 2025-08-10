package com.ellie.capstone.repository;

import com.ellie.capstone.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

    // Find weather record by location (case insensitive)
    Optional<Weather> findByLocationIgnoreCase(String location);
}