package com.ellie.capstone.controller;

import com.ellie.capstone.model.Project;
import com.ellie.capstone.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")  // Base path
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    // POST /api/projects
    @PostMapping
    public Project createProject(@RequestBody Project project) {
        return service.createProject(project);
    }

    // GET /api/projects
    @GetMapping
    public List<Project> getAllProjects() {
        return service.getAllProjects();
    }

}
