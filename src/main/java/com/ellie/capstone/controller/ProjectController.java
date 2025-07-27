package com.ellie.capstone.controller;

import com.ellie.capstone.model.Project;
import com.ellie.capstone.service.ProjectService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/projects")  // Base path
@Validated
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

//    // POST /api/projects w/o error handler
//    @PostMapping
//
//    public Project createProject(@RequestBody Project project) {
//        return service.createProject(project);
//    }

    // POST /api/projects with error handler
    @PostMapping
    public ResponseEntity<?> createProject(@Valid @RequestBody Project project) {
        try {
            Project saved = service.createProject(project);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    // GET /api/projects
    @GetMapping
    public List<Project> getAllProjects() {
        return service.getAllProjects();
    }

}
