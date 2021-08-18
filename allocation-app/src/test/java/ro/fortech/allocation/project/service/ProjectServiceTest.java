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
import ro.fortech.allocation.assignments.dto.ProjectAssignmentDto;
import ro.fortech.allocation.assignments.model.Assignment;
import ro.fortech.allocation.assignments.repository.AssignmentRepository;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.project.dto.ProjectAssignmentsDto;
import ro.fortech.allocation.project.dto.ProjectRequestDto;
import ro.fortech.allocation.project.dto.ProjectResponseDto;
import ro.fortech.allocation.project.exception.ProjectNotFoundException;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.project.repository.ProjectRepository;
import ro.fortech.allocation.technology.model.Technology;
import ro.fortech.allocation.technology.repository.TechnologyRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {
    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private TechnologyRepository technologyRepository;

    @Mock
    private ModelMapper mapper;

    @Test
    public void getProjects_givenProjects_expectTheProjects() throws ParseException {
        Project project = ProjectFactory.getProject();
        ProjectResponseDto projectResponseDto = ProjectFactory.getProjectResponseDto();
        Technology technology = new Technology();
        technology.setName("name");
        technology.setExternalId("externalId");
        ro.fortech.allocation.technology.dto.TechnologyDto technologyDto = new ro.fortech.allocation.technology.dto.TechnologyDto();
        technologyDto.setName("name");
        technologyDto.setExternalId("externalId");
        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> page = new PageImpl<>(Collections.singletonList(project));
        when(projectRepository.findAll(pageable)).thenReturn(page);
        when(mapper.map(project, ProjectResponseDto.class)).thenReturn(projectResponseDto);

        Page<ProjectResponseDto> projectsPage = projectService.getProjects(pageable);
        assertEquals(1, projectsPage.getTotalElements());
    }

    @Test
    public void getProjectByExternalId_givenExternalId_expectTheProject() throws ParseException {
        ProjectResponseDto projectResponseDto = ProjectFactory.getProjectResponseDto();
        Project project = ProjectFactory.getProject();

        Technology technology = new Technology();
        technology.setName("name");
        technology.setExternalId("externalId");
        ro.fortech.allocation.technology.dto.TechnologyDto technologyDto = new ro.fortech.allocation.technology.dto.TechnologyDto();
        technologyDto.setName("name");
        technologyDto.setExternalId("externalId");

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
    public void getAssignmentsOfProject_givenExternalId_expectAssignments() throws ParseException {
        Project project = ProjectFactory.getProject();

        when(projectRepository.findProjectByExternalId(project.getExternalId())).thenReturn(Optional.of(project));

        Assignment assignment = Assignment.builder()
                .employee(Employee.builder()
                        .uid("22")
                        .email("adsa@yahoo.com")
                        .name("aaa")
                        .active(true)
                        .internalPosition("aaaa")
                        .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                        .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                        .build())
                .project(ProjectFactory.getProject())
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .projectPosition(".net")
                .allocationHours(8)
                .build();

        Set<Assignment> projectAssignment = new HashSet<>();
        projectAssignment.add(assignment);

        when(assignmentRepository.findAssignmentByProject(project.getId())).thenReturn(projectAssignment);

        ProjectAssignmentsDto result = new ProjectAssignmentsDto();
        result.setProjectName(project.getName());
        result.setProjectExternalId(project.getExternalId());
        List<ProjectAssignmentDto> assignmentDtoList = new ArrayList<>();
        for (Assignment a : projectAssignment) {

            ProjectAssignmentDto temp = new ProjectAssignmentDto(
                    a.getUid(),
                    a.getEmployee().getName(),
                    a.getEmployee().getUid(),
                    a.getStartDate(),
                    a.getEndDate(),
                    a.getProjectPosition(),
                    a.getAllocationHours());
            assignmentDtoList.add(temp);
        }
        result.setAssignments(assignmentDtoList);

        ProjectAssignmentsDto actualResponse = projectService.getAssignmentsOfAProject(project.getExternalId());

        assertEquals(result.getAssignments().size(), actualResponse.getAssignments().size());
        assertEquals(result.getProjectName(), actualResponse.getProjectName());
    }

    @Test
    public void createProject_givenProjectDto_expectTheCreatedProject() throws ParseException {
        ProjectRequestDto projectRequestDto = ProjectFactory.getProjectRequestDto();
        ProjectResponseDto projectResponseDto = ProjectFactory.getProjectResponseDto();
        Project project = ProjectFactory.getProject();

        Technology technology = new Technology();
        technology.setName("name");
        technology.setExternalId("externalId");
        ro.fortech.allocation.technology.dto.TechnologyDto technologyDto = new ro.fortech.allocation.technology.dto.TechnologyDto();
        technologyDto.setName("name");
        technologyDto.setExternalId("externalId");


        projectResponseDto.setExternalId(projectRequestDto.getExternalId());
        project.setExternalId(projectRequestDto.getExternalId());

        when(mapper.map(projectRequestDto, Project.class)).thenReturn(project);
        when(mapper.map(project, ProjectResponseDto.class)).thenReturn(projectResponseDto);
        when(technologyRepository.findByExternalId(any(String.class))).thenReturn(Optional.of(technology));
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

        Technology technology = new Technology();
        technology.setName("name");
        technology.setExternalId("externalId");
        ro.fortech.allocation.technology.dto.TechnologyDto technologyDto = new ro.fortech.allocation.technology.dto.TechnologyDto();
        technologyDto.setName("name");
        technologyDto.setExternalId("externalId");

        projectResponseDto.setExternalId(projectRequestDto.getExternalId());
        project.setExternalId(projectRequestDto.getExternalId());

        projectRequestDto.setClient("New");
        projectResponseDto.setClient("New");

        when(projectRepository.findProjectByExternalId(project.getExternalId())).thenReturn(Optional.of(project));
        when(mapper.map(projectRequestDto, Project.class)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(mapper.map(project, ProjectResponseDto.class)).thenReturn(projectResponseDto);
        when(technologyRepository.findByExternalId(any(String.class))).thenReturn(Optional.of(technology));

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