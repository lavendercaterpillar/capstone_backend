package com.ellie.capstone.service;

import com.ellie.capstone.exception.ResourceNotFoundException;
import com.ellie.capstone.model.Project;
import com.ellie.capstone.model.Weather;
import com.ellie.capstone.repository.ProjectRepository;
import com.ellie.capstone.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final WeatherService weatherService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                          WeatherService weatherService) {
        this.projectRepository = projectRepository;
        this.weatherService = weatherService;
    }

    @Transactional
    public Project createProject(Project project) {
        if (project.getLocation() != null) {
            Weather weather = weatherService.getWeatherForLocation(project.getLocation());
            project.setWeather(weather);
        }
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> filterProjects(String name, String location) {
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
        Project existing = getProjectById(id); // Ensures project exists

        // Update weather if location changed
        if (updatedProject.getLocation() != null &&
                !updatedProject.getLocation().equals(existing.getLocation())) {
            Weather weather = weatherService.getWeatherForLocation(updatedProject.getLocation());
            existing.setWeather(weather);
            existing.setLocation(updatedProject.getLocation());
        }

        // Update core project fields
        existing.setProjectName(updatedProject.getProjectName());
        existing.setArea(updatedProject.getArea());

        // Update all wall areas and window counts
        existing.setNorthWallArea(updatedProject.getNorthWallArea());
        existing.setNorthWindowCount(updatedProject.getNorthWindowCount());

        existing.setSouthWallArea(updatedProject.getSouthWallArea());
        existing.setSouthWindowCount(updatedProject.getSouthWindowCount());

        existing.setEastWallArea(updatedProject.getEastWallArea());
        existing.setEastWindowCount(updatedProject.getEastWindowCount());

        existing.setWestWallArea(updatedProject.getWestWallArea());
        existing.setWestWindowCount(updatedProject.getWestWindowCount());

        return projectRepository.save(existing);
    }

    public void deleteProject(Long id) {
        getProjectById(id); // Ensures project exists
        projectRepository.deleteById(id);
    }

}
