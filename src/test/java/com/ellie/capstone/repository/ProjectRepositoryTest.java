package com.ellie.capstone.repository;

import com.ellie.capstone.model.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    private Project createProject(String name, String location) {
        return new Project(name, location, 100.0,
                10.0, 2,
                10.0, 2,
                10.0, 2,
                10.0, 2);
    }

    @Test
    @DisplayName("Should find projects by projectName ignoring case")
    void findByProjectNameContainingIgnoreCase_shouldReturnProjects() {
        projectRepository.save(createProject("Solar Build", "Seattle"));
        List<Project> results = projectRepository.findByProjectNameContainingIgnoreCase("solar");
        assertThat(results).hasSize(1);
    }

    @Test
    @DisplayName("Should find projects by location ignoring case")
    void findByLocationContainingIgnoreCase_shouldReturnProjects() {
        projectRepository.save(createProject("Wind Tower", "Portland"));
        List<Project> results = projectRepository.findByLocationContainingIgnoreCase("port");
        assertThat(results).hasSize(1);
    }

    @Test
    @DisplayName("Should find projects by name and location ignoring case")
    void findByProjectNameAndLocationContainingIgnoreCase_shouldReturnProjects() {
        projectRepository.save(createProject("Green Roof", "Boston"));
        List<Project> results =
                projectRepository.findByProjectNameContainingIgnoreCaseAndLocationContainingIgnoreCase("green", "bost");
        assertThat(results).hasSize(1);
    }
}
