package ro.fortech.allocation.project.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.fortech.allocation.ProjectFactory;
import ro.fortech.allocation.project.model.Project;

import java.text.ParseException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void findProjectByExternalId_givenExternalId_expectOptionalOfTheProject() throws ParseException {
        Project project = ProjectFactory.getProject();
        projectRepository.save(project);
        assertEquals(Optional.of(project), projectRepository.findProjectByExternalId(project.getExternalId()));
    }

    @Test
    public void deleteProjectByExternalId_givenExternalId_expectNoProjects() throws ParseException {
        Project project = ProjectFactory.getProject();
        projectRepository.save(project);
        assertEquals(1, projectRepository.findAll().size());
        projectRepository.deleteProjectByExternalId(project.getExternalId());
        assertEquals(0, projectRepository.findAll().size());
    }

    @Test
    public void existsProjectByExternalId_givenProject_expectTheProject() throws ParseException {
        Project project = ProjectFactory.getProject();
        projectRepository.save(project);
        assertTrue(projectRepository.existsProjectByExternalId(project.getExternalId()));
        assertFalse(projectRepository.existsProjectByExternalId(UUID.randomUUID().toString()));
    }
}