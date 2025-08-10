package com.ellie.capstone.service;

import com.ellie.capstone.exception.ResourceNotFoundException;
import com.ellie.capstone.model.Project;
import com.ellie.capstone.model.Weather;
import com.ellie.capstone.repository.ProjectRepository;
import com.ellie.capstone.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceUnitTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private WeatherService weatherService;

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project testProject;

    @BeforeEach
    void setUp() {
        testProject = new Project("Test Project", "New York", 1000.0,
                200.0, 2, 200.0, 2, 200.0, 2, 200.0, 2);
    }

    @Test
    void saveProject_shouldEnrichWithWeatherAndSave() {
        // Setup test data
        Project project = new Project("Test Project", "New York", 1000.0,
                200.0, 2, 200.0, 2, 200.0, 2, 200.0, 2);

        Weather mockWeather = new Weather();
        mockWeather.setLocation("New York");

        // Mock repository behavior
        when(weatherService.findByLocation("New York"))
                .thenReturn(Optional.of(mockWeather));
        when(projectRepository.save(any(Project.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Execute
        Project savedProject = projectService.saveProject(project);

        // Verify
        assertNotNull(savedProject);
        assertEquals(mockWeather, savedProject.getWeather());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void getAllProjects_shouldReturnFilteredProjects() {
        when(projectRepository.findByProjectNameContainingIgnoreCaseAndLocationContainingIgnoreCase("test", "york"))
                .thenReturn(List.of(testProject));

        List<Project> projects = projectService.getAllProjects("test", "york");

        assertEquals(1, projects.size());
        assertEquals("Test Project", projects.get(0).getProjectName());
    }

    @Test
    void getProjectById_shouldReturnProjectWhenExists() {
        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(testProject));

        Project found = projectService.getProjectById(1L);

        assertEquals("Test Project", found.getProjectName());
    }

    @Test
    void getProjectById_shouldThrowWhenNotFound() {
        when(projectRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            projectService.getProjectById(1L);
        });
    }

    @Test
    void updateProject_shouldUpdateExistingProject() {
        Project updatedProject = new Project("Updated", "Chicago", 1500.0,
                300.0, 3, 300.0, 3, 300.0, 3, 300.0, 3);

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Project result = projectService.updateProject(1L, updatedProject);

        assertEquals("Updated", result.getProjectName());
        assertEquals("Chicago", result.getLocation());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void deleteProject_shouldDeleteWhenExists() {
        // Setup test data
        Project project = new Project("Test Project", "New York", 1000.0,
                200.0, 2, 200.0, 2, 200.0, 2, 200.0, 2);
        project.setId(1L);

        // Mock repository behavior
        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));
        doNothing().when(projectRepository).deleteById(1L);

        // Execute
        projectService.deleteProject(1L);

        // Verify
        verify(projectRepository).deleteById(1L);
    }
}