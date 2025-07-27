package com.ellie.capstone.controller;

import com.ellie.capstone.model.Project;
import com.ellie.capstone.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

//    // POST /api/projects w/o error handler
//    @PostMapping
//    public Project createProject(@RequestBody Project project) {
//        return service.createProject(project);
//    }

    // POST /api/projects — validation errors handled globally
    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) {
        Project saved = service.createProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET /api/projects
    @GetMapping
    public List<Project> getAllProjects() {
        return service.getAllProjects();
    }

}
