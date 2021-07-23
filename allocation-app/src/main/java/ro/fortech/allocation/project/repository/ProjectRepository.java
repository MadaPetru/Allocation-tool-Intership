package ro.fortech.allocation.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.fortech.allocation.project.service.model.Project;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findProjectByExternalId(String externalId);
    void deleteProjectByExternalId(String externalId);
    boolean existsProjectByExternalId(String externalId);
}