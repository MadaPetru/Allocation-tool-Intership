package ro.fortech.allocation.projecttechnologystack.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ro.fortech.allocation.ProjectFactory;
import ro.fortech.allocation.project.exception.ProjectNotFoundException;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.projecttechnologystack.dto.ProjectTechnologyStackDto;
import ro.fortech.allocation.projecttechnologystack.exception.ProjectTechnologyStackNotFoundException;
import ro.fortech.allocation.projecttechnologystack.model.ProjectTechnologyStack;
import ro.fortech.allocation.projecttechnologystack.model.ProjectTechnologyStackKey;
import ro.fortech.allocation.projecttechnologystack.service.ProjectTechnologyStackService;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;
import ro.fortech.allocation.technology.model.Technology;

import javax.validation.ConstraintViolation;
import java.text.ParseException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@EnableWebMvc
public class ProjectTechnologyStackControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private LocalValidatorFactoryBean validator;

    @MockBean
    ProjectTechnologyStackService service;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addProjectTechnology_givenProjectAndTechnology_expectProjectTechnology() throws Exception {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);

        when(service.addProjectTechnology(Mockito.any(ProjectTechnologyStackDto.class))).thenReturn(dto);

        mockMvc.perform(post("/projectTechnology")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(service).addProjectTechnology(dto);
    }

    @Test
    public void addProjectTechnology_givenProjectAndTechnology_expectProjectNotFound() throws Exception {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        String projectExternalId = projectTechnologyStack.getProject().getExternalId();
        when(service.addProjectTechnology(Mockito.any(ProjectTechnologyStackDto.class)))
                .thenThrow(new ProjectNotFoundException(projectExternalId));
        mockMvc.perform(post("/projectTechnology")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service).addProjectTechnology(dto);
    }

    @Test
    public void addProjectTechnology_givenProjectAndTechnology_expectTechnologyNotFound() throws Exception {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        String technologyExternalId = projectTechnologyStack.getTechnology().getExternalId();
        when(service.addProjectTechnology(Mockito.any(ProjectTechnologyStackDto.class)))
                .thenThrow(new TechnologyNotFoundByExternalIdException(technologyExternalId));

        mockMvc.perform(post("/projectTechnology")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service).addProjectTechnology(dto);
    }

    @Test
    public void addProjectTechnology_givenProjectAndTechnology_expectProjectTechnologyNotFound() throws Exception {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        when(service.addProjectTechnology(Mockito.any(ProjectTechnologyStackDto.class)))
                .thenThrow(new ProjectTechnologyStackNotFoundException("Not found"));
        mockMvc.perform(post("/projectTechnology")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service).addProjectTechnology(dto);
    }

    @Test
    public void deleteProjectTechnology_givenProjectTechnologyExternalId_expectStatusOk() throws Exception {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        mockMvc.perform(delete("/projectTechnology")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(service).deleteProjectTechnology(dto);
    }

    @Test
    public void deleteProjectTechnology_givenProjectTechnologyExternalId_expectProjectNotFound() throws Exception {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        String projectExternalId = projectTechnologyStack.getProject().getExternalId();
        mockMvc.perform(delete("/projectTechnology")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(service).deleteProjectTechnology(dto);
    }

    @Test
    public void deleteProjectTechnology_givenProjectTechnologyExternalId_expectTechnologyNotFound() throws Exception {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        String technologyExternalId = projectTechnologyStack.getTechnology().getExternalId();
        mockMvc.perform(delete("/projectTechnology")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(service).deleteProjectTechnology(dto);
    }

    @Test
    public void deleteProjectTechnology_givenProjectTechnologyExternalId_expectProjectTechnologyNotFound() throws Exception {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        mockMvc.perform(delete("/projectTechnology")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(service).deleteProjectTechnology(dto);
    }

    @Test
    public void getAllTechnologiesFromProject_givenProjectExternalId_expectTechnologies() throws Exception {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        String projectExternalId = projectTechnologyStack.getProject().getExternalId();
        List<ro.fortech.allocation.technology.dto.TechnologyDto> list = new LinkedList<>();
        mockMvc.perform(get("/projectTechnology/" + projectExternalId)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getAllTechnologiesFromProject_givenProjectExternalId_expectProject() throws Exception {
        ProjectTechnologyStack projectTechnologyStack = makeEntity();
        ProjectTechnologyStackDto dto = mapper.map(projectTechnologyStack, ProjectTechnologyStackDto.class);
        String projectExternalId = projectTechnologyStack.getProject().getExternalId();
        List<ro.fortech.allocation.technology.dto.TechnologyDto> list = new LinkedList<>();
        mockMvc.perform(get("/projectTechnology/" + projectExternalId)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void validation_givenNonValid_expectConstraintViolation() throws ParseException {
        ProjectTechnologyStackDto dto = mapper.map(makeEntity(), ProjectTechnologyStackDto.class);
        ProjectTechnologyStackDto nonValidDto = ProjectTechnologyStackDto.builder()
                .projectExternalId("")
                .technologyExternalId("")
                .build();
        Set<ConstraintViolation<ProjectTechnologyStackDto>> constraintViolationSet =
                validator.validateProperty(nonValidDto, "projectExternalId");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertEquals("name can not be empty", constraintViolationSet.iterator().next().getMessage());

        Assertions.assertEquals(1, constraintViolationSet.size());
        constraintViolationSet = validator.validateProperty(nonValidDto, "technologyExternalId");
        Assertions.assertEquals("name can not be empty", constraintViolationSet.iterator().next().getMessage());

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