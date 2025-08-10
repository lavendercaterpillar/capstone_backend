package com.ellie.capstone;

import com.ellie.capstone.model.Project;
import com.ellie.capstone.model.Weather;
import com.ellie.capstone.repository.ProjectRepository;
import com.ellie.capstone.repository.WeatherRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest
public class ProjectWeatherRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private WeatherRepository weatherRepository;

    @Test
    void testPersistAndRetrieveWeather() {
        Weather weather = new Weather("Miami", 90.0, 70.0, 75.0, 80.0);
        Weather savedWeather = weatherRepository.save(weather);

        Optional<Weather> found = weatherRepository.findById(savedWeather.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getLocation()).isEqualTo("Miami");
        assertThat(found.get().getAvgSummerTemp()).isEqualTo(90.0);
        assertThat(found.get().getWetBulbTemp()).isEqualTo(75.0);
    }

    @Test
    void testPersistAndRetrieveProjectWithWeather() {
        // Create and save Weather first
        Weather weather = new Weather("Seattle", 75.0, 50.0, 55.0, 60.0);
        weather = weatherRepository.save(weather);

        // Create Project and set weather relationship
        Project project = new Project(
                "Eco Building", "Seattle", 1200.0,
                300.0, 5, 400.0, 4,
                250.0, 3, 250.0, 6);
        project.setWeather(weather);

        // Save Project
        Project savedProject = projectRepository.save(project);

        // Retrieve Project from DB and check fields + relationship
        Optional<Project> foundProject = projectRepository.findById(savedProject.getId());
        assertThat(foundProject).isPresent();

        Project p = foundProject.get();

        // Check basic fields
        assertThat(p.getProjectName()).isEqualTo("Eco Building");
        assertThat(p.getLocation()).isEqualTo("Seattle");
        assertThat(p.getArea()).isEqualTo(1200.0);
        assertThat(p.getNorthWallArea()).isEqualTo(300.0);
        assertThat(p.getNorthWindowCount()).isEqualTo(5);

        // Check Weather relationship
        Weather w = p.getWeather();
        assertThat(w).isNotNull();
        assertThat(w.getLocation()).isEqualTo("Seattle");
        assertThat(w.getAvgWinterTemp()).isEqualTo(50.0);
    }
}
