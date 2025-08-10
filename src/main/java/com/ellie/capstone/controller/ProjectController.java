package com.ellie.capstone.controller;

import com.ellie.capstone.exception.ResourceNotFoundException;
import com.ellie.capstone.model.Project;
import com.ellie.capstone.model.Weather;
import com.ellie.capstone.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) {
        // Weather data automatically handled in service layer
        Project saved = projectService.createProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Project> filterProjects(
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String location
    ) {
        List<Project> projects = projectService.filterProjects(projectName, location);
        if (projects.isEmpty()) {
            throw new ResourceNotFoundException("No matching projects found.");
        }
        return projects;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        if (project == null) {
            throw new ResourceNotFoundException("Project with ID " + id + " not found.");
        }
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody Project updatedProject) {

        Project existing = projectService.getProjectById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Project with ID " + id + " not found.");
        }

        // Weather updates automatically handled in service layer
        Project saved = projectService.updateProject(id, updatedProject);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        Project existing = projectService.getProjectById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Project with ID " + id + " not found.");
        }

        projectService.deleteProject(id);
        return ResponseEntity.ok("Project with ID " + id + " deleted successfully.");
    }

    // NEW ENDPOINTS FOR WEATHER INTEGRATION

    @GetMapping("/{id}/weather")
    public ResponseEntity<Weather> getProjectWeather(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        if (project == null || project.getWeather() == null) {
            throw new ResourceNotFoundException("Weather data not available for project ID " + id);
        }
        return ResponseEntity.ok(project.getWeather());
    }

}
