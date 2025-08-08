package com.ellie.capstone.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Project name is required")
    @Column(name = "project_name")
    private String projectName;

    @NotBlank(message = "Location is required")
    @Column(name = "location")
    private String location;

    @Positive(message = "Area must be greater than 0")
    @Column(name = "area")
    private Double area;

    @Positive(message = "North wall Area must be greater than 0")
    @Column(name = "north_wall")
    private Double northWallArea;

    @NotNull(message = "North window count is required")
    @Column(name = "north_window")
    private Integer northWindowCount;

    @Positive(message = "South wall Area must be greater than 0")
    @Column(name = "south_wall")
    private Double southWallArea;

    @NotNull(message = "South window count is required")
    @Column(name = "south_window")
    private Integer southWindowCount;

    @Positive(message = "East wall Area must be greater than 0")
    @Column(name = "east_wall")
    private Double eastWallArea;

    @NotNull(message = "East window count is required")
    @Column(name = "east_window")
    private Integer eastWindowCount;

    @Positive(message = "West wall Area must be greater than 0")
    @Column(name = "west_wall")
    private Double westWallArea;

    @NotNull(message = "West window count is required")
    @Column(name = "west_window")
    private Integer westWindowCount;

    // many to one join
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weather_id")
    private Weather weather;

    // Constructors
    public Project() {}

    // Update your constructor to reflect the new field names
    public Project(String projectName, String location, double area,
                   double northWallArea, int northWindowCount,
                   double westWallArea, int westWindowCount,
                   double southWallArea, int southWindowCount,
                   double eastWallArea, int eastWindowCount) {
        this.projectName = projectName;
        this.location = location;
        this.area = area;
        this.northWallArea = northWallArea;
        this.northWindowCount = northWindowCount;
        this.westWallArea = westWallArea;
        this.westWindowCount = westWindowCount;
        this.southWallArea = southWallArea;
        this.southWindowCount = southWindowCount;
        this.eastWallArea = eastWallArea;
        this.eastWindowCount = eastWindowCount;
    }

    // Getters and Setters for ALL fields
      public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getNorthWallArea() {
        return northWallArea;
    }

    public void setNorthWallArea(double northWallArea) {
        this.northWallArea = northWallArea;
    }

    public int getNorthWindowCount() {
        return northWindowCount;
    }

    public void setNorthWindowCount(int northWindowCount) {
        this.northWindowCount = northWindowCount;
    }

    public double getWestWallArea() {
        return westWallArea;
    }

    public void setWestWallArea(double westWallArea) {
        this.westWallArea = westWallArea;
    }

    public int getWestWindowCount() {
        return westWindowCount;
    }

    public void setWestWindowCount(int westWindowCount) {
        this.westWindowCount = westWindowCount;
    }

    public double getSouthWallArea() {
        return southWallArea;
    }

    public void setSouthWallArea(double southWallArea) {
        this.southWallArea = southWallArea;
    }

    public int getSouthWindowCount() {
        return southWindowCount;
    }

    public void setSouthWindowCount(int southWindowCount) {
        this.southWindowCount = southWindowCount;
    }

    public double getEastWallArea() {
        return eastWallArea;
    }

    public void setEastWallArea(double eastWallArea) {
        this.eastWallArea = eastWallArea;
    }

    public int getEastWindowCount() {
        return eastWindowCount;
    }

    public void setEastWindowCount(int eastWindowCount) {
        this.eastWindowCount = eastWindowCount;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

}