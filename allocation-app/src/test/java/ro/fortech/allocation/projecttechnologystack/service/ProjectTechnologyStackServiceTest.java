package ro.fortech.allocation.projecttechnologystack.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import ro.fortech.allocation.ProjectFactory;
import ro.fortech.allocation.project.exception.ProjectNotFoundException;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.project.repository.ProjectRepository;
import ro.fortech.allocation.projecttechnologystack.dto.ProjectTechnologyStackDto;
import ro.fortech.allocation.projecttechnologystack.exception.ProjectTechnologyStackAlreadyExistException;
import ro.fortech.allocation.projecttechnologystack.exception.ProjectTechnologyStackNotFoundException;
import ro.fortech.allocation.projecttechnologystack.model.ProjectTechnologyStack;
import ro.fortech.allocation.projecttechnologystack.model.ProjectTechnologyStackKey;
import ro.fortech.allocation.projecttechnologystack.repository.ProjectTechnologyStackRepository;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;
import ro.fortech.allocation.technology.model.Technology;
import ro.fortech.allocation.technology.repository.TechnologyRepository;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProjectTechnologyStackServiceTest {
    @Mock
    private ModelMapper modelMapper;

    private final ModelMapper mapper = new ModelMapper();

    @InjectMocks
    ProjectTechnologyStackService service;

    @Mock
    ProjectTechnologyStackRepository repository;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    TechnologyRepository technologyRepository;

    @Test
    public void addProjectTechnology_givenProjectAndTechnology_expectProjectTechnology() throws ParseException {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        Project project = projectTechnologyStack.getProject();
        Technology technology = projectTechnologyStack.getTechnology();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        when(projectRepository.findProjectByExternalId(any(String.class))).thenReturn(java.util.Optional.of(project));
        when(technologyRepository.findByExternalId(any(String.class))).thenReturn(java.util.Optional.of(technology));
        when(repository.existsByProjectAndTechnology(any(Project.class), any(Technology.class)))
                .thenReturn(false);
        when(repository.save(any(ProjectTechnologyStack.class))).thenReturn(projectTechnologyStack);
        service.addProjectTechnology(dto);
    }

    @Test
    public void deleteProjectTechnology_givenExternalIdProjectAndTechnology_expectDeleted() throws ParseException {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        Project project = projectTechnologyStack.getProject();
        Technology technology = projectTechnologyStack.getTechnology();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        when(projectRepository.findProjectByExternalId(any(String.class))).thenReturn(Optional.of(project));
        when(technologyRepository.findByExternalId(any(String.class))).thenReturn(Optional.of(technology));
        when(repository.findByProjectAndTechnology(Mockito.any(Project.class), Mockito.any(Technology.class)))
                .thenReturn(Optional.ofNullable(projectTechnologyStack));
        service.deleteProjectTechnology(mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class));
        verify(repository).delete(projectTechnologyStack);
        assertThat(repository.findById(projectTechnologyStack.getId()).isPresent()).isFalse();
    }

    @Test(expected = ProjectNotFoundException.class)
    public void addProjectTechnology_givenProjectAndTechnology_expectNotFoundProject() throws ParseException {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        Project project = projectTechnologyStack.getProject();
        Technology technology = projectTechnologyStack.getTechnology();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        String projectExternalId = project.getExternalId();
        when(projectRepository.findProjectByExternalId(any(String.class)))
                .thenThrow(new ProjectNotFoundException(projectExternalId));
        service.addProjectTechnology(dto);
    }

    @Test(expected = TechnologyNotFoundByExternalIdException.class)
    public void addProjectTechnology_givenProjectAndTechnology_expectNotFoundTechnology() throws ParseException {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        Project project = projectTechnologyStack.getProject();
        Technology technology = projectTechnologyStack.getTechnology();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        String technologyExternalId = technology.getExternalId();
        when(projectRepository.findProjectByExternalId(any(String.class)))
                .thenReturn(Optional.of(project));
        when(technologyRepository.findByExternalId(any(String.class)))
                .thenThrow(new TechnologyNotFoundByExternalIdException(technologyExternalId));
        service.addProjectTechnology(dto);
    }

    @Test(expected = ProjectTechnologyStackAlreadyExistException.class)
    public void addProjectTechnology_givenProjectAndTechnology_expectAlreadyExistException() throws ParseException {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        Project project = projectTechnologyStack.getProject();
        Technology technology = projectTechnologyStack.getTechnology();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        when(projectRepository.findProjectByExternalId(any(String.class)))
                .thenReturn(Optional.of(project));
        when(technologyRepository.findByExternalId(any(String.class)))
                .thenReturn(Optional.of(technology));
        when(repository.existsByProjectAndTechnology(any(Project.class), any(Technology.class)))
                .thenReturn(true);
        service.addProjectTechnology(dto);
    }

    @Test(expected = ProjectNotFoundException.class)
    public void deleteProjectTechnology_givenExternalIdProjectAndTechnology_expectProjectNotFound() throws ParseException {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        Project project = projectTechnologyStack.getProject();
        Technology technology = projectTechnologyStack.getTechnology();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        String projectExternalId = project.getExternalId();
        when(projectRepository.findProjectByExternalId(any(String.class)))
                .thenThrow(new ProjectNotFoundException(projectExternalId));
        service.deleteProjectTechnology(mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class));
    }

    @Test(expected = TechnologyNotFoundByExternalIdException.class)
    public void deleteProjectTechnology_givenExternalIdProjectAndTechnology_expectNotFoundTechnology() throws ParseException {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        Project project = projectTechnologyStack.getProject();
        Technology technology = projectTechnologyStack.getTechnology();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        String technologyExternalId = technology.getExternalId();
        when(projectRepository.findProjectByExternalId(any(String.class))).thenReturn(Optional.of(project));
        when(technologyRepository.findByExternalId(any(String.class)))
                .thenThrow(new TechnologyNotFoundByExternalIdException(technologyExternalId));
        service.deleteProjectTechnology(mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class));
    }

    @Test(expected = ProjectTechnologyStackNotFoundException.class)
    public void deleteProjectTechnology_givenExternalIdProjectAndTechnology_expectNotFoundProjectTechnology() throws ParseException {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        Project project = projectTechnologyStack.getProject();
        Technology technology = projectTechnologyStack.getTechnology();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        when(projectRepository.findProjectByExternalId(any(String.class))).thenReturn(Optional.of(project));
        when(technologyRepository.findByExternalId(any(String.class))).thenReturn(Optional.of(technology));
        when(repository.findByProjectAndTechnology(Mockito.any(Project.class), Mockito.any(Technology.class)))
                .thenThrow(new ProjectTechnologyStackNotFoundException("Not Found"));
        service.deleteProjectTechnology(mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class));
    }

    private ProjectTechnologyStack makeEntity() throws ParseException {
        Set<ProjectTechnologyStack> set = new HashSet<>();
        Project project = ProjectFactory.getProject();
        Technology technology = Technology.builder()
                .id(1L)
                .name("name")
                .externalId("externalId")
                .build();
        ProjectTechnologyStack projectTechnologyStack = ProjectTechnologyStack.builder()
                .project(project)
                .technology(technology)
                .id(new ProjectTechnologyStackKey(project.getId(), technology.getId()))
                .build();
        project.setExternalId("externalId");
        return projectTechnologyStack;
    }
}