package com.ellie.capstone.service;

import com.ellie.capstone.model.Project;
import com.ellie.capstone.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ProjectService {

    private final ProjectRepository repo;

    public ProjectService(ProjectRepository repo) {
        this.repo = repo;
    }

    public Project createProject(Project project) {
        return repo.save(project);  // save to DB
    }

    public List<Project> getAllProjects() {
        return repo.findAll();
    }

}
