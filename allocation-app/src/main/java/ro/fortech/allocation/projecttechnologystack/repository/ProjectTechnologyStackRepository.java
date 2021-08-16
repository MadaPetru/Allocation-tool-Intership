package ro.fortech.allocation.projecttechnologystack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.projecttechnologystack.model.ProjectTechnologyStack;
import ro.fortech.allocation.projecttechnologystack.model.ProjectTechnologyStackKey;
import ro.fortech.allocation.technology.model.Technology;

import java.util.Optional;

@Repository
public interface ProjectTechnologyStackRepository extends JpaRepository<ProjectTechnologyStack, ProjectTechnologyStackKey> {
    boolean existsByProjectAndTechnology(Project project, Technology technology);
    Optional<ProjectTechnologyStack> findByProjectAndTechnology(Project project, Technology technology);
}
