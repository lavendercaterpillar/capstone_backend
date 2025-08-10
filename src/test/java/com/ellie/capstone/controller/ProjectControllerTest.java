package com.ellie.capstone.controller;

import com.ellie.capstone.exception.ResourceNotFoundException;
import com.ellie.capstone.model.Project;
import com.ellie.capstone.model.Weather;
import com.ellie.capstone.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    // Sample Weather for linking
    private Weather createSampleWeather() {
        Weather weather = new Weather("Seattle", 75.0, 50.0, 55.0, 60.0);
        weather.setId(1L);
        return weather;
    }

    // Sample Project with linked Weather
    private Project createSampleProject() {
        Project project = new Project(
                "Eco Building", "Seattle", 1200.0,
                300.0, 5, 400.0, 4,
                250.0, 3, 250.0, 6);
        project.setId(1L);
        project.setWeather(createSampleWeather());
        return project;
    }

    @Test
    void testCreateProjectSuccess() throws Exception {
        Project inputProject = createSampleProject();
        inputProject.setId(null); // ID null for creation

        Project savedProject = createSampleProject();

        given(projectService.saveProject(any(Project.class))).willReturn(savedProject);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputProject)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(savedProject.getId()))
                .andExpect(jsonPath("$.projectName").value("Eco Building"))
                .andExpect(jsonPath("$.weather.location").value("Seattle"));
    }

    @Test
    void testCreateProjectValidationError() throws Exception {
        Project invalidProject = createSampleProject();
        invalidProject.setProjectName(""); // Blank name triggers validation error

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProject)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.projectName").value("Project name is required"));
    }

    @Test
    void testGetAllProjectsSuccess() throws Exception {
        Project project1 = createSampleProject();
        Project project2 = createSampleProject();
        project2.setId(2L);
        project2.setProjectName("Another Project");

        List<Project> projects = List.of(project1, project2);

        given(projectService.getAllProjects(null, null)).willReturn(projects);

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(projects.size()))
                .andExpect(jsonPath("$[0].id").value(project1.getId()))
                .andExpect(jsonPath("$[1].projectName").value("Another Project"));
    }

    @Test
    void testGetAllProjectsNotFound() throws Exception {
        given(projectService.getAllProjects(null, null)).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No projects found"));
    }

    @Test
    void testGetProjectByIdSuccess() throws Exception {
        Project project = createSampleProject();

        given(projectService.getProjectById(1L)).willReturn(project);

        mockMvc.perform(get("/api/projects/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(project.getId()))
                .andExpect(jsonPath("$.projectName").value("Eco Building"))
                .andExpect(jsonPath("$.weather.location").value("Seattle"));
    }

    @Test
    void testGetProjectByIdNotFound() throws Exception {
        given(projectService.getProjectById(1L))
                .willThrow(new ResourceNotFoundException("Project not found with id 1"));

        mockMvc.perform(get("/api/projects/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Project not found with id 1"));
    }

    @Test
    void testUpdateProjectSuccess() throws Exception {
        Project updateData = createSampleProject();
        updateData.setProjectName("Updated Project");

        given(projectService.updateProject(eq(1L), any(Project.class))).willReturn(updateData);

        mockMvc.perform(put("/api/projects/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectName").value("Updated Project"));
    }

    @Test
    void testUpdateProjectValidationError() throws Exception {
        Project invalidUpdate = createSampleProject();
        invalidUpdate.setProjectName("");

        mockMvc.perform(put("/api/projects/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.projectName").value("Project name is required"));
    }

    @Test
    void testDeleteProjectSuccess() throws Exception {
        // Since deleteProject returns void, just mock it to do nothing
        willDoNothing().given(projectService).deleteProject(1L);

        mockMvc.perform(delete("/api/projects/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Project deleted successfully"));
    }
}