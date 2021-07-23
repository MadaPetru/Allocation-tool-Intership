package ro.fortech.allocation.project.service;

import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.fortech.allocation.ProjectFactory;
import ro.fortech.allocation.project.dto.ProjectRequestDto;
import ro.fortech.allocation.project.dto.ProjectResponseDto;
import ro.fortech.allocation.project.repository.ProjectRepository;
import ro.fortech.allocation.project.service.model.Project;

import java.util.ArrayList;
import java.util.List;
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
    public void getAllProjects() {
        Project project1 = this.getProject();
        Project project2 = this.getProject();

        List<Project> projects = new ArrayList<>();
        projects.add(project1);
        projects.add(project2);

        when(projectRepository.findAll()).thenReturn(projects);


        List<ProjectResponseDto> projectResponseDtos = projectService.getAllProjects();

        assertEquals(projects.size(),projectResponseDtos.size());

    }

    @Test
    public void getProjectByExternalId() {

        ProjectResponseDto projectResponseDto = this.getProjectResponseDto();
        Project project = this.getProject();
        project.setExternalId(projectResponseDto.getExternalId());


        when(projectRepository.findProjectByExternalId(project.getExternalId())).thenReturn(Optional.of(project));

        when(projectService.toProjectResponseDto(project)).thenReturn(projectResponseDto);

        assertEquals(projectResponseDto, projectService.getProjectByExternalId(projectResponseDto.getExternalId()));


    }

    @Test
    public void getProjectByExternalId_whenProjectDoesntExist_expectIllegalStateException() {
        Project project = this.getProject();


        when(projectRepository.findProjectByExternalId(project.getExternalId())).thenReturn(Optional.empty());



        Exception exception = assertThrows(IllegalStateException.class,
                () -> projectService.getProjectByExternalId(project.getExternalId()));

        String expectedMessage = "Project with projectId " + project.getExternalId() + " was not found!";


        assertEquals(expectedMessage, exception.getMessage());


    }


    @Test
    public void createProject() {

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
    public void updateProject() {

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
    public void updateProject_whenProjectDoesntExist_expectIllegalStateException(){
        Project project = this.getProject();
        ProjectRequestDto projectRequestDto = this.getProjectRequestDto();


        when(projectRepository.findProjectByExternalId(project.getExternalId())).thenReturn(Optional.empty());



        Exception exception = assertThrows(IllegalStateException.class,
                () -> projectService.updateProject(project.getExternalId(), projectRequestDto));

        String expectedMessage = "Project with id " + project.getExternalId() + " was not found!";


        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void deleteProject() {

        Project project = this.getProject();

        when(projectRepository.existsProjectByExternalId(project.getExternalId())).thenReturn(true);

        projectService.deleteProject(project.getExternalId());

        verify(projectRepository,times(1)).deleteProjectByExternalId(project.getExternalId());
    }

    @Test
    public void deleteProject_whenProjectDoesntExist_expectIllegalStateException(){
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
    public void toProjectResponseDto() {

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
    public void toProjectRequestDto() {
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
    public void projectRequestDtoToProject() {

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
    public void projectResponseDtoToProject() {

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