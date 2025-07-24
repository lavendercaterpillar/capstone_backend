package com.ellie.capstone.repository;

import com.ellie.capstone.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    // No need to define anything for basic CRUD
}
