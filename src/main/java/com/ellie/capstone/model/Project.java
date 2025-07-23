package com.ellie.capstone.model;

import jakarta.persistence.*;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectName;
    private String location;

    private double area;

    private double northWallArea;
    private int northWallWindows;

    private double westWallArea;
    private int westWallWindows;

    private double southWallArea;
    private int southWallWindows;

    private double eastWallArea;
    private int eastWallWindows;

    // 🔧 Constructors
    public Project() {}

    public Project(String projectName, String location, double area,
                   double northWallArea, int northWallWindows,
                   double westWallArea, int westWallWindows,
                   double southWallArea, int southWallWindows,
                   double eastWallArea, int eastWallWindows) {
        this.projectName = projectName;
        this.location = location;
        this.area = area;
        this.northWallArea = northWallArea;
        this.northWallWindows = northWallWindows;
        this.westWallArea = westWallArea;
        this.westWallWindows = westWallWindows;
        this.southWallArea = southWallArea;
        this.southWallWindows = southWallWindows;
        this.eastWallArea = eastWallArea;
        this.eastWallWindows = eastWallWindows;
    }

    // 🧼 Getters and Setters (Generate or use Lombok)
    // IntelliJ: Right-click → Generate → Getter and Setter
}
