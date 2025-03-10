package ro.fortech.allocation.project.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ro.fortech.allocation.ProjectFactory;
import ro.fortech.allocation.assignments.dto.ProjectAssignmentDto;
import ro.fortech.allocation.project.dto.ProjectAssignmentsDto;
import ro.fortech.allocation.project.dto.ProjectRequestDto;
import ro.fortech.allocation.project.dto.ProjectResponseDto;
import ro.fortech.allocation.project.service.ProjectService;
import ro.fortech.allocation.technology.dto.TechnologyDto;

import javax.validation.ConstraintViolation;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@EnableSpringDataWebSupport
public class ProjectControllerTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String PROJECTS_URL = "/projects";

    @MockBean
    private ProjectService projectService;

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public final void setup() {
        TimeZone defaultTimeZone = TimeZone.getTimeZone("UTC");
        TimeZone.setDefault(defaultTimeZone);
    }

    @Test
    public void getProjects_givenProjects_expectTheProjects() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProjectResponseDto> page = new PageImpl<>(Collections.singletonList(ProjectFactory.getProjectResponseDto()));
        when(projectService.getProjects(pageable)).thenReturn(page);
        mockMvc.perform(get(PROJECTS_URL)
                .content(objectMapper.writeValueAsString(pageable))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(projectService).getProjects(Mockito.any(Pageable.class));
    }

    @Test
    public void validation_givenNonValid_expectConstraintViolation(){
        ProjectRequestDto nonValidProjectRequestDto = ProjectFactory.getInvalidProjectRequestDto();
        Set<ConstraintViolation<ProjectRequestDto>> constraintViolationSet = validator.validateProperty(nonValidProjectRequestDto, "name");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertEquals("length must be between 2 and 255", constraintViolationSet.iterator().next().getMessage());

        constraintViolationSet = validator.validateProperty(nonValidProjectRequestDto, "client");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertTrue(constraintViolationSet.iterator().next().getMessage().contains("length must be between 2 and 255"));

        constraintViolationSet = validator.validateProperty(nonValidProjectRequestDto, "description");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertTrue(constraintViolationSet.iterator().next().getMessage().contains("length must be between 2 and 2000"));

        constraintViolationSet = validator.validateProperty(nonValidProjectRequestDto, "technicalStack");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertTrue(constraintViolationSet.iterator().next().getMessage().contains("length must be between 2 and 500"));

        constraintViolationSet = validator.validateProperty(nonValidProjectRequestDto, "startDate");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertEquals("must not be null", constraintViolationSet.iterator().next().getMessage());

        constraintViolationSet = validator.validateProperty(nonValidProjectRequestDto, "endDate");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertEquals("must not be null", constraintViolationSet.iterator().next().getMessage());
    }

    @Test
    public void getAssignmentsOfAProject_givenExternalId_expectAssignments() throws Exception {
        ProjectAssignmentDto assignmentDto = ProjectAssignmentDto.builder()
                .uid("a")
                .employeeName("Employee")
                .employeeUid("e")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .projectPosition("position")
                .allocationHours(8.3)
                .build();

        List<ProjectAssignmentDto> assignmentDtos = new ArrayList<>();
        assignmentDtos.add(assignmentDto);

        ProjectAssignmentsDto projectAssignmentsDto = ProjectAssignmentsDto.builder()
                .projectExternalId("externalId")
                .projectName("name")
                .assignments(assignmentDtos)
                .build();

        Mockito.when(
                projectService.getAssignmentsOfAProject("externalId")).thenReturn(projectAssignmentsDto);


        mockMvc.perform(get(PROJECTS_URL + "/externalId/assignments")
                .content(objectMapper.writeValueAsString(projectAssignmentsDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectName").value("name"));
    }

    @Test
    public void getProjectByExternalId_givenExternalId_expectTheProject() throws Exception {
        when(projectService.getProjectByExternalId(ProjectFactory.getProject().getExternalId())).thenReturn(ProjectFactory.getProjectResponseDto());
        mockMvc.perform(get(PROJECTS_URL + "/" + ProjectFactory.getProject().getExternalId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(ProjectFactory.getProjectResponseDto().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.client").value(ProjectFactory.getProjectResponseDto().getClient()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(ProjectFactory.getProjectResponseDto().getDescription()));
        verify(projectService, times(1)).getProjectByExternalId(ProjectFactory.getProject().getExternalId());
    }

    @Test
    public void createProject_givenProject_expectTheCreatedProject() throws Exception {
        when(projectService.createProject(ProjectFactory.getProjectRequestDto())).thenReturn(ProjectFactory.getProjectResponseDto());
        mockMvc.perform(post(PROJECTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ProjectFactory.getProjectRequestDto())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(ProjectFactory.getProjectResponseDto().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.client").value(ProjectFactory.getProjectResponseDto().getClient()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(ProjectFactory.getProjectResponseDto().getDescription()))
                .andExpect(status().isOk());

        verify(projectService, times(1)).createProject(ProjectFactory.getProjectRequestDto());
    }

    @Test
    public void updateProject_givenExternalIdAndProjectToUpdate_expectUpdatedProject() throws Exception {
        when(projectService.updateProject(any(String.class), any(ProjectRequestDto.class))).thenReturn(ProjectFactory.getProjectResponseDto());
        mockMvc.perform(put(PROJECTS_URL + "/" + ProjectFactory.getProjectRequestDto().getExternalId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ProjectFactory.getProjectResponseDto())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(ProjectFactory.getProjectResponseDto().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.client").value(ProjectFactory.getProjectResponseDto().getClient()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(ProjectFactory.getProjectResponseDto().getDescription()))
                .andExpect(status().isOk());

        verify(projectService, times(1)).updateProject(ProjectFactory.getProjectRequestDto().getExternalId(), ProjectFactory.getProjectRequestDto());
    }

    @Test
    public void deleteProject_givenExternalId_expectStatusOk() throws Exception {
        ProjectRequestDto projectRequestDto = ProjectFactory.getProjectRequestDto();

        mockMvc.perform(delete(PROJECTS_URL + "/" + projectRequestDto.getExternalId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andExpect(status().isOk());
        verify(projectService, times(1)).deleteProject(projectRequestDto.getExternalId());
    }

    @Test
    public void addTechnologyToProject_expectProjectWithAddedTechnology() throws Exception {
        ProjectResponseDto projectResponseDto = ProjectFactory.getProjectResponseDto();
        TechnologyDto someTech = TechnologyDto.builder().name("SomeTech").externalId("SomeExternalUID").build();

        projectResponseDto.getTechnologyDtos().add(someTech);

        Iterator iterator = projectResponseDto.getTechnologyDtos().iterator();

        when(projectService.addTechnologyToProject(projectResponseDto.getExternalId(), someTech.getExternalId())).thenReturn(projectResponseDto);

        mockMvc.perform(patch("/projects/" + projectResponseDto.getExternalId())
                        .param("technologyUid", "SomeExternalUID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.technologyDtos[0]").value(iterator.next()))
                .andExpect(jsonPath("$.technologyDtos[1]").value(iterator.next()));
    }

}