package ro.fortech.allocation.project.service;

import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import ro.fortech.allocation.ProjectFactory;
import ro.fortech.allocation.project.dto.ProjectRequestDto;
import ro.fortech.allocation.project.dto.ProjectResponseDto;
import ro.fortech.allocation.project.repository.ProjectRepository;
import ro.fortech.allocation.project.model.Project;
import java.text.ParseException;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DataJpaTest
@RunWith(SpringRunner.class)
@RequiredArgsConstructor

public class ProjectServiceTest extends ProjectFactory {
    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void getProjectsTest() throws ParseException {
        ProjectResponseDto projectResponseDto = this.getProjectResponseDto();
        Project project = this.getProject();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> page = new PageImpl<>(Collections.singletonList(this.getProject()));
        when(projectRepository.findAll(pageable)).thenReturn(page);

        Page<ProjectResponseDto> projectsPage = projectService.getProjects(pageable);
        assertEquals(1, projectsPage.getTotalElements());
    }

    @Test
    public void getProjectByExternalIdTest() throws ParseException {
        ProjectResponseDto projectResponseDto = this.getProjectResponseDto();
        Project project = this.getProject();
        project.setExternalId(projectResponseDto.getExternalId());

        when(projectRepository.findProjectByExternalId(project.getExternalId())).thenReturn(Optional.of(project));
        when(projectService.toProjectResponseDto(project)).thenReturn(projectResponseDto);

        assertEquals(projectResponseDto, projectService.getProjectByExternalId(projectResponseDto.getExternalId()));
    }

    @Test
    public void getProjectByExternalId_whenProjectDoesntExist_expectIllegalStateExceptionTest() throws ParseException {
        Project project = this.getProject();

        when(projectRepository.findProjectByExternalId(project.getExternalId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalStateException.class,
                () -> projectService.getProjectByExternalId(project.getExternalId()));

        String expectedMessage = "Project with projectId " + project.getExternalId() + " was not found!";

        assertEquals(expectedMessage, exception.getMessage());
    }


    @Test
    public void createProjectTest() throws ParseException {

        ProjectRequestDto projectRequestDto = this.getProjectRequestDto();
        ProjectResponseDto projectResponseDto = this.getProjectResponseDto();
        Project project = this.getProject();

        projectResponseDto.setExternalId(projectRequestDto.getExternalId());
        project.setExternalId(projectRequestDto.getExternalId());

        when(projectService.ProjectRequestDtoToProject(projectRequestDto)).thenReturn(project);
        when(projectService.toProjectResponseDto(project)).thenReturn(projectResponseDto);
        when(projectRepository.save(project)).thenReturn(project);

        ProjectResponseDto result = projectService.createProject(projectRequestDto);

        assertEquals(projectResponseDto.getClient(), result.getClient());
        assertEquals(projectResponseDto.getDescription(), result.getDescription());
        assertEquals(projectResponseDto.getName(), result.getName());
    }

    @Test
    public void updateProjectTest() throws ParseException {

        ProjectRequestDto projectRequestDto = this.getProjectRequestDto();
        ProjectResponseDto projectResponseDto = this.getProjectResponseDto();
        Project project = this.getProject();

        projectResponseDto.setExternalId(projectRequestDto.getExternalId());
        project.setExternalId(projectRequestDto.getExternalId());

        projectRequestDto.setClient("New");
        projectResponseDto.setClient("New");

        when(projectRepository.findProjectByExternalId(project.getExternalId())).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);
        when(projectService.toProjectResponseDto(project)).thenReturn(projectResponseDto);

        ProjectResponseDto result = projectService.updateProject(projectRequestDto.getExternalId(), projectRequestDto);
        assertEquals(projectRequestDto.getClient(), result.getClient());
        assertEquals(projectRequestDto.getName(), result.getName());
        assertEquals(projectRequestDto.getExternalId(), result.getExternalId());
    }

    @Test
    public void updateProject_whenProjectDoesntExist_expectIllegalStateExceptionTest() throws ParseException {
        Project project = this.getProject();
        ProjectRequestDto projectRequestDto = this.getProjectRequestDto();

        when(projectRepository.findProjectByExternalId(project.getExternalId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalStateException.class,
                () -> projectService.updateProject(project.getExternalId(), projectRequestDto));

        String expectedMessage = "Project with id " + project.getExternalId() + " was not found!";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void deleteProjectTest() throws ParseException {
        Project project = this.getProject();

        when(projectRepository.existsProjectByExternalId(project.getExternalId())).thenReturn(true);
        projectService.deleteProject(project.getExternalId());
        verify(projectRepository,times(1)).deleteProjectByExternalId(project.getExternalId());
    }

    @Test
    public void deleteProject_whenProjectDoesntExist_expectIllegalStateExceptionTest() throws ParseException {
        Project project = this.getProject();
        ProjectRequestDto projectRequestDto = this.getProjectRequestDto();
        projectRequestDto.setExternalId(project.getExternalId());

        when(projectRepository.existsProjectByExternalId(project.getExternalId())).thenReturn(false);

        Exception exception = assertThrows(IllegalStateException.class,
                () -> projectService.deleteProject(project.getExternalId()));
        String expectedMessage = "Project with id " + project.getExternalId() + " does not exist!";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void toProjectResponseDtoTest() throws ParseException {
        Project project = this.getProject();
        ProjectResponseDto projectResponseDto = this.getProjectResponseDto();
        projectResponseDto.setExternalId(project.getExternalId());

        when(modelMapper.map(project, ProjectResponseDto.class)).thenReturn(projectResponseDto);

        ProjectResponseDto projectResponseDto1 = projectService.toProjectResponseDto(project);

        assertEquals(project.getClient(), projectResponseDto1.getClient());
        assertEquals(project.getName(), projectResponseDto1.getName());
        assertEquals(project.getDescription(), projectResponseDto1.getDescription());
    }

    @Test
    public void toProjectRequestDtoTest() throws ParseException {
        Project project = this.getProject();
        ProjectRequestDto projectRequestDto = this.getProjectRequestDto();
        projectRequestDto.setExternalId(project.getExternalId());

        when(modelMapper.map(project, ProjectRequestDto.class)).thenReturn(projectRequestDto);

        ProjectRequestDto projectRequestDto1 = projectService.toProjectRequestDto(project);

        assertEquals(project.getClient(), projectRequestDto1.getClient());
        assertEquals(project.getName(), projectRequestDto1.getName());
        assertEquals(project.getDescription(), projectRequestDto1.getDescription());
    }

    @Test
    public void projectRequestDtoToProjectTest() throws ParseException {
        Project project = this.getProject();
        ProjectRequestDto projectRequestDto = this.getProjectRequestDto();
        projectRequestDto.setExternalId(project.getExternalId());

        when(modelMapper.map(projectRequestDto, Project.class)).thenReturn(project);
        Project project1 = projectService.ProjectRequestDtoToProject(projectRequestDto);

        assertEquals(project1.getClient(), projectRequestDto.getClient());
        assertEquals(project1.getName(), projectRequestDto.getName());
        assertEquals(project1.getDescription(), projectRequestDto.getDescription());
    }

    @Test
    public void projectResponseDtoToProjectTest() throws ParseException {
        Project project = this.getProject();
        ProjectResponseDto projectResponseDto = this.getProjectResponseDto();
        projectResponseDto.setExternalId(project.getExternalId());

        when(modelMapper.map(projectResponseDto, Project.class)).thenReturn(project);
        Project project1 = projectService.ProjectResponseDtoToProject(projectResponseDto);

        assertEquals(project1.getClient(), projectResponseDto.getClient());
        assertEquals(project1.getName(), projectResponseDto.getName());
        assertEquals(project1.getDescription(), projectResponseDto.getDescription());
    }
}