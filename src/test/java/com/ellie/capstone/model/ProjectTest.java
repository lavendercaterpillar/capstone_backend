package com.ellie.capstone.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailValidationWhenProjectNameBlank() {
        Project project = new Project();
        project.setProjectName("");
        project.setLocation("Seattle");
        project.setArea(100.0);
        project.setNorthWallArea(10.0);
        project.setNorthWindowCount(2);
        project.setSouthWallArea(10.0);
        project.setSouthWindowCount(2);
        project.setEastWallArea(10.0);
        project.setEastWindowCount(2);
        project.setWestWallArea(10.0);
        project.setWestWindowCount(2);

        Set<ConstraintViolation<Project>> violations = validator.validate(project);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("projectName"));
    }

    @Test
    void areaMustBePositive() {
        Project p = new Project();
        p.setProjectName("Valid Name");
        p.setLocation("Valid Location");
        p.setArea(-1.0);  // invalid
        p.setNorthWallArea(10.0);
        p.setNorthWindowCount(1);
        p.setSouthWallArea(10.0);
        p.setSouthWindowCount(1);
        p.setEastWallArea(10.0);
        p.setEastWindowCount(1);
        p.setWestWallArea(10.0);
        p.setWestWindowCount(1);

        Set<ConstraintViolation<Project>> violations = validator.validate(p);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("area"));
    }

    @Test
    void testWallAndWindowAttributes() {
        Project project = new Project();

        project.setNorthWallArea(100.5);
        project.setNorthWindowCount(2);
        project.setSouthWallArea(120.0);
        project.setSouthWindowCount(3);
        project.setEastWallArea(80.5);
        project.setEastWindowCount(1);
        project.setWestWallArea(90.0);
        project.setWestWindowCount(4);

        assertEquals(100.5, project.getNorthWallArea());
        assertEquals(2, project.getNorthWindowCount());
        assertEquals(120.0, project.getSouthWallArea());
        assertEquals(3, project.getSouthWindowCount());
        assertEquals(80.5, project.getEastWallArea());
        assertEquals(1, project.getEastWindowCount());
        assertEquals(90.0, project.getWestWallArea());
        assertEquals(4, project.getWestWindowCount());
    }

    @Test
    void testProjectWeatherRelationship() {
        Weather weather = new Weather(
                "New York",
                30.0,
                -5.0,
                25.0,
                20.0
        );

        Project project = new Project();
        project.setProjectName("Test Project");
        project.setLocation("New York");
        project.setWeather(weather);

        assertNotNull(project.getWeather());
        assertEquals("New York", project.getWeather().getLocation());
        assertEquals(30.0, project.getWeather().getAvgSummerTemp());
        assertEquals(-5.0, project.getWeather().getAvgWinterTemp());
    }

    @Test
    void gettersAndSettersShouldWork() {
        Project project = new Project();
        project.setProjectName("Test");
        assertThat(project.getProjectName()).isEqualTo("Test");
    }
}
