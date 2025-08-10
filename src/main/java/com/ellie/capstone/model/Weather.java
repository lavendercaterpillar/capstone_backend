package com.ellie.capstone.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "weather")
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique location string to identify weather data for a place
    @Column(name = "location", unique = true, nullable = false)
    private String location;

    @NotNull(message = "Average summer temperature is required")
    @Column(name = "avg_summer_temp")
    private Double avgSummerTemp;

    @NotNull(message = "Average winter temperature is required")
    @Column(name = "avg_winter_temp")
    private Double avgWinterTemp;

    @NotNull(message = "Wet bulb temperature is required")
    @Column(name = "wet_bulb_temp")
    private Double wetBulbTemp;

    @NotNull(message = "Dry bulb temperature is required")
    @Column(name = "dry_bulb_temp")
    private Double dryBulbTemp;

    // Constructors
    public Weather() {}

    public Weather(String location, Double avgSummerTemp, Double avgWinterTemp,
                   Double wetBulbTemp, Double dryBulbTemp) {
        this.location = location;
        this.avgSummerTemp = avgSummerTemp;
        this.avgWinterTemp = avgWinterTemp;
        this.wetBulbTemp = wetBulbTemp;
        this.dryBulbTemp = dryBulbTemp;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getAvgSummerTemp() {
        return avgSummerTemp;
    }

    public void setAvgSummerTemp(Double avgSummerTemp) {
        this.avgSummerTemp = avgSummerTemp;
    }

    public Double getAvgWinterTemp() {
        return avgWinterTemp;
    }

    public void setAvgWinterTemp(Double avgWinterTemp) {
        this.avgWinterTemp = avgWinterTemp;
    }

    public Double getWetBulbTemp() {
        return wetBulbTemp;
    }

    public void setWetBulbTemp(Double wetBulbTemp) {
        this.wetBulbTemp = wetBulbTemp;
    }

    public Double getDryBulbTemp() {
        return dryBulbTemp;
    }

    public void setDryBulbTemp(Double dryBulbTemp) {
        this.dryBulbTemp = dryBulbTemp;
    }
}
