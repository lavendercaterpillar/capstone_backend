package com.ellie.capstone.repository;

import com.ellie.capstone.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    // This gives you findAll(), save(), and other CRUD methods — no need to write them yourself!
    // like SQLAlchemy in Flask for basic CRUD
}
