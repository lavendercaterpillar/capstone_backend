package com.ellie.capstone.service;

import com.ellie.capstone.exception.ResourceNotFoundException;
import com.ellie.capstone.model.Project;
import com.ellie.capstone.model.Weather;
import com.ellie.capstone.repository.ProjectRepository;
import com.ellie.capstone.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final WeatherService weatherService;
    private final WeatherRepository weatherRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                          WeatherService weatherService,
                          WeatherRepository weatherRepository) {
        this.projectRepository = projectRepository;
        this.weatherService = weatherService;
        this.weatherRepository = weatherRepository;
    }

    public Project saveProject(Project project) {
        // Enrich with weather data
        Weather weather = weatherService.findByLocation(project.getLocation())
                .orElseGet(() -> {
                    Map<String, Object> weatherData = weatherService.getWeatherSummary(project.getLocation());
                    Weather newWeather = new Weather();
                    newWeather.setLocation(project.getLocation());
                    newWeather.setDryBulbTemp((Double) weatherData.get("dryBulbF"));
                    newWeather.setWetBulbTemp((Double) weatherData.get("wetBulbF"));
                    newWeather.setAvgSummerTemp((Double) weatherData.get("avgSummerF"));
                    newWeather.setAvgWinterTemp((Double) weatherData.get("avgWinterF"));
                    return weatherService.save(newWeather);
                });

        project.setWeather(weather);
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects(String name, String location) {
        if (name != null && location != null) {
            return projectRepository.findByProjectNameContainingIgnoreCaseAndLocationContainingIgnoreCase(name, location);
        } else if (name != null) {
            return projectRepository.findByProjectNameContainingIgnoreCase(name);
        } else if (location != null) {
            return projectRepository.findByLocationContainingIgnoreCase(location);
        }
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    public Project updateProject(Long id, Project updatedProject) {
        getProjectById(id); // Ensures project exists
        updatedProject.setId(id);
        enrichProjectWithWeather(updatedProject);
        return projectRepository.save(updatedProject);
    }

    public void deleteProject(Long id) {
        getProjectById(id); // Ensures project exists
        projectRepository.deleteById(id);
    }

    private void enrichProjectWithWeather(Project project) {
        if (project.getLocation() == null || project.getLocation().isBlank()) {
            return;
        }

        Optional<Weather> optionalWeather = weatherService.findByLocation(project.getLocation());

        Weather weather;

        if (optionalWeather.isPresent()) {
            // Update existing weather data for location
            weather = optionalWeather.get();
            updateWeatherFromApi(weather);
        } else {
            // Create new weather data for location
            weather = new Weather();
            weather.setLocation(project.getLocation());
            updateWeatherFromApi(weather);
        }

        // Save or update weather entity
        weather = weatherService.save(weather);

        // Link project to weather entity
        project.setWeather(weather);
    }

    private void updateWeatherFromApi(Weather weather) {
        Map<String, Object> weatherData = weatherService.getWeatherSummary(weather.getLocation());
        if (weatherData != null) {
            weather.setAvgSummerTemp((Double) weatherData.get("avgSummerF"));
            weather.setAvgWinterTemp((Double) weatherData.get("avgWinterF"));
            weather.setWetBulbTemp((Double) weatherData.get("wetBulbF"));
            weather.setDryBulbTemp((Double) weatherData.get("dryBulbF"));
        }
    }
}
