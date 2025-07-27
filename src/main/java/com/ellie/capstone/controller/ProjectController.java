package com.ellie.capstone.controller;

import com.ellie.capstone.model.Project;
import com.ellie.capstone.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")  // Base path
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
    public ResponseEntity<?> createProject(@Valid @RequestBody Project project, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError error : fieldErrors) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Project saved = service.createProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET /api/projects
    @GetMapping
    public List<Project> getAllProjects() {
        return service.getAllProjects();
    }

}
