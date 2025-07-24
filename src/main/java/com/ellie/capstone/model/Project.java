package com.ellie.capstone.model;

import jakarta.persistence.*;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "location")
    private String location;

    @Column(name = "area")
    private double area;

    @Column(name = "north_wall_area")
    private double northWallArea;

    @Column(name = "north_wall_windows") 
    private int northWindowCount;

    @Column(name = "west_wall_area")
    private double westWallArea;

    @Column(name = "west_wall_windows") 
    private int westWindowCount; 

    @Column(name = "south_wall_area")
    private double southWallArea;

    @Column(name = "south_wall_windows") 
    private int southWindowCount; 

    @Column(name = "east_wall_area")
    private double eastWallArea;

    @Column(name = "east_wall_windows") 
    private int eastWindowCount; 

    // 🔧 Constructors
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

    // 🧼 Getters and Setters for ALL fields
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
}