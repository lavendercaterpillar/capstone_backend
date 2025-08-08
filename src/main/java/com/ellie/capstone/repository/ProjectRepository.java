package com.ellie.capstone.repository;

import com.ellie.capstone.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//public interface ProjectRepository extends JpaRepository<Project, Long> {
//    // This gives you findAll(), save(), and other CRUD methods — no need to write them yourself!
//    // like SQLAlchemy in Flask for basic CRUD
//}

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Optional filtering methods if needed later
    // Find projects by projectName containing (case-insensitive)
    List<Project> findByProjectNameContainingIgnoreCase(String projectName);

    // Find projects by location containing (case-insensitive)
    List<Project> findByLocationContainingIgnoreCase(String location);

    // Find projects by both projectName and location containing (case-insensitive)
    List<Project> findByProjectNameContainingIgnoreCaseAndLocationContainingIgnoreCase(String projectName, String location);
}
