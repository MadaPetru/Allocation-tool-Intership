package ro.fortech.allocation.projecttechnologystack.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.fortech.allocation.ProjectFactory;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.project.repository.ProjectRepository;
import ro.fortech.allocation.projecttechnologystack.model.ProjectTechnologyStack;
import ro.fortech.allocation.projecttechnologystack.model.ProjectTechnologyStackKey;
import ro.fortech.allocation.technology.model.Technology;
import ro.fortech.allocation.technology.repository.TechnologyRepository;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProjectTechnologyStackRepositoryTest {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TechnologyRepository technologyRepository;

    @Autowired
    ProjectTechnologyStackRepository repository;

    @Test
    public void findByProjectAndTechnology() throws ParseException {
        Set<ProjectTechnologyStack> set = new HashSet<>();
        Project project = ProjectFactory.getProject();
        Technology technology = Technology.builder()
                .name("name")
                .externalId("externalId")
                .build();
        projectRepository.save(project);
        technologyRepository.save(technology);
        ProjectTechnologyStack projectTechnologyStack = ProjectTechnologyStack.builder()
                .project(project)
                .technology(technology)
                .id(new ProjectTechnologyStackKey(project.getId(), technology.getId()))
                .build();
        ProjectTechnologyStack response = repository.save(projectTechnologyStack);
        assertThat(projectTechnologyStack.getTechnology().getName()).isEqualTo(response.getTechnology().getName());
    }

    @Test
    public void existByProjectAndTechnology() throws ParseException {
        Set<ProjectTechnologyStack> set = new HashSet<>();
        Project project = ProjectFactory.getProject();
        Technology technology = Technology.builder()
                .name("name")
                .externalId("externalId")
                .build();
        projectRepository.save(project);
        technologyRepository.save(technology);
        ProjectTechnologyStack projectTechnologyStack = ProjectTechnologyStack.builder()
                .project(project)
                .technology(technology)
                .id(new ProjectTechnologyStackKey(project.getId(), technology.getId()))
                .build();
        repository.save(projectTechnologyStack);
    }
}