package ro.fortech.allocation.project.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ro.fortech.allocation.ProjectFactory;
import ro.fortech.allocation.project.dto.ProjectRequestDto;
import ro.fortech.allocation.project.dto.ProjectResponseDto;
import ro.fortech.allocation.project.exception.ProjectNotFoundException;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.project.repository.ProjectRepository;

import java.text.ParseException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {
    @InjectMocks;


    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ModelMapper mapper;

    @Test
    public void getProjects_givenProjects_expectTheProjects() throws ParseException {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> page = new PageImpl<>(Collections.singletonList(ProjectFactory.getProject()));
        when(projectRepository.findAll(pageable)).thenReturn(page);

        Page<ProjectResponseDto> projectsPage = projectService.getProjects(pageable);
        assertEquals(1, projectsPage.getTotalElements());
    }

    @Test
    public void getProjectByExternalId_givenExternalId_expectTheProject() throws ParseException {
        ProjectResponseDto projectResponseDto = ProjectFactory.getProjectResponseDto();
        Project project = ProjectFactory.getProject();
        project.setExternalId(projectResponseDto.getExternalId());

        when(projectRepository.findProjectByExternalId(project.getExternalId())).thenReturn(Optional.of(project));
        when(mapper.map(project, ProjectResponseDto.class)).thenReturn(projectResponseDto);

        assertEquals(projectResponseDto, projectService.getProjectByExternalId(projectResponseDto.getExternalId()));
    }

    @Test
    public void getProjectByExternalId_whenProjectDoesntExist_expectProjectNotFoundException() throws ParseException {
        Project project = ProjectFactory.getProject();

        when(projectRepository.findProjectByExternalId(project.getExternalId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProjectNotFoundException.class,
                () -> projectService.getProjectByExternalId(project.getExternalId()));

        String expectedMessage = "Could not find project with externalId: " + project.getExternalId();

        assertEquals(expectedMessage, exception.getMessage());
    }


    @Test
    public void createProject_givenProjectDto_expectTheCreatedProject() throws ParseException {
        ProjectRequestDto projectRequestDto = ProjectFactory.getProjectRequestDto();
        ProjectResponseDto projectResponseDto = ProjectFactory.getProjectResponseDto();
        Project project = ProjectFactory.getProject();

        projectResponseDto.setExternalId(projectRequestDto.getExternalId());
        project.setExternalId(projectRequestDto.getExternalId());

        when(mapper.map(projectRequestDto, Project.class)).thenReturn(project);
        when(mapper.map(project, ProjectResponseDto.class)).thenReturn(projectResponseDto);
        when(projectRepository.save(project)).thenReturn(project);

        ProjectResponseDto result = projectService.createProject(projectRequestDto);

        assertEquals(projectResponseDto.getClient(), result.getClient());
        assertEquals(projectResponseDto.getDescription(), result.getDescription());
        assertEquals(projectResponseDto.getName(), result.getName());
    }

    @Test
    public void updateProject_givenExternalIdAndProjectToUpdate_expectTheProject() throws ParseException {

        ProjectRequestDto projectRequestDto = ProjectFactory.getProjectRequestDto();
        ProjectResponseDto projectResponseDto = ProjectFactory.getProjectResponseDto();
        Project project = ProjectFactory.getProject();

        projectResponseDto.setExternalId(projectRequestDto.getExternalId());
        project.setExternalId(projectRequestDto.getExternalId());

        projectRequestDto.setClient("New");
        projectResponseDto.setClient("New");

        when(projectRepository.findProjectByExternalId(project.getExternalId())).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);
        when(mapper.map(project, ProjectResponseDto.class)).thenReturn(projectResponseDto);

        ProjectResponseDto result = projectService.updateProject(projectRequestDto.getExternalId(), projectRequestDto);
        assertEquals(projectRequestDto.getClient(), result.getClient());
        assertEquals(projectRequestDto.getName(), result.getName());
        assertEquals(projectRequestDto.getExternalId(), result.getExternalId());
    }

    @Test
    public void updateProject_whenProjectDoesntExist_expectProjectNotFoundException() throws ParseException {
        Project project = ProjectFactory.getProject();
        ProjectRequestDto projectRequestDto = ProjectFactory.getProjectRequestDto();

        when(projectRepository.findProjectByExternalId(project.getExternalId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProjectNotFoundException.class,
                () -> projectService.updateProject(project.getExternalId(), projectRequestDto));

        String expectedMessage = "Could not find project with externalId: " + project.getExternalId();

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void deleteProject_givenExternalId_expectStatusOk() throws ParseException {
        Project project = ProjectFactory.getProject();

        when(projectRepository.findProjectByExternalId(project.getExternalId())).thenReturn(Optional.of(project));
        projectService.deleteProject(project.getExternalId());
        verify(projectRepository, times(1)).delete(project);
    }

    @Test
    public void deleteProject_whenProjectDoesntExist_expectProjectNotFoundException() throws ParseException {
        Project project = ProjectFactory.getProject();
        ProjectRequestDto projectRequestDto = ProjectFactory.getProjectRequestDto();
        projectRequestDto.setExternalId(project.getExternalId());
        when(projectRepository.findProjectByExternalId(project.getExternalId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ProjectNotFoundException.class,
                () -> projectService.deleteProject(project.getExternalId()));
        String expectedMessage = "Could not find project with externalId: " + project.getExternalId();
        assertEquals(expectedMessage, exception.getMessage());
    }
}