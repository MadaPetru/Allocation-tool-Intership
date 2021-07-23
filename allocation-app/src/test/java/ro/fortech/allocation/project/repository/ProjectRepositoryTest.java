package ro.fortech.allocation.project.repository;

import lombok.RequiredArgsConstructor;
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

@DataJpaTest
@RunWith(SpringRunner.class)
@RequiredArgsConstructor

public class ProjectRepositoryTest extends ProjectFactory {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void findProjectByExternalIdTest() throws ParseException {
        Project project = this.getProject();
        projectRepository.save(project);
        assertEquals(Optional.of(project), projectRepository.findProjectByExternalId(project.getExternalId()));
    }

    @Test
    public void deleteProjectByExternalIdTest() throws ParseException {
        Project project = this.getProject();
        projectRepository.save(project);
        assertEquals(1, projectRepository.findAll().size());
        projectRepository.deleteProjectByExternalId(project.getExternalId());
        assertEquals(0, projectRepository.findAll().size());
    }

    @Test
    public void existsProjectByExternalIdTest() throws ParseException {
        Project project = this.getProject();

        projectRepository.save(project);

        assertTrue(projectRepository.existsProjectByExternalId(project.getExternalId()));
        assertFalse(projectRepository.existsProjectByExternalId( UUID.randomUUID().toString()));
    }
}