package ro.fortech.allocation.project.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ro.fortech.allocation.ProjectFactory;
import ro.fortech.allocation.project.dto.ProjectRequestDto;
import ro.fortech.allocation.project.dto.ProjectResponseDto;
import ro.fortech.allocation.project.service.ProjectService;

import java.text.ParseException;
import java.util.Collections;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@EnableSpringDataWebSupport
public class ProjectApiControllerTest extends ProjectFactory {

    public ProjectApiControllerTest() throws ParseException {
    }

    @Before
    public void setup() {
        TimeZone defaultTimeZone = TimeZone.getTimeZone("UTC");
        TimeZone.setDefault(defaultTimeZone);
    }

    @MockBean
    private ProjectService projectService;

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String PROJECTS_URL = "/projects";

    @Test
    public void getProjectsTest() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProjectResponseDto> page = new PageImpl<>(Collections.singletonList(this.getProjectResponseDto()));
        when(projectService.getProjects(pageable)).thenReturn(page);
        mockMvc.perform(get(PROJECTS_URL)
                .content(objectMapper.writeValueAsString(pageable))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(projectService).getProjects(Mockito.any(Pageable.class));
    }

    @Test
    public void getProjectByExternalIdTest() throws Exception {
        when(projectService.getProjectByExternalId(this.getProject().getExternalId())).thenReturn(this.getProjectResponseDto());
        mockMvc.perform(get(PROJECTS_URL + "/" + this.getProject().getExternalId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(this.getProjectResponseDto().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.client").value(this.getProjectResponseDto().getClient()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(this.getProjectResponseDto().getDescription()));
        verify(projectService, times(1)).getProjectByExternalId(this.getProject().getExternalId());
        assertEquals(1, 1);
    }

    @Test
    public void createProjectTest() throws Exception {
        when(projectService.createProject(this.getProjectRequestDto())).thenReturn(this.getProjectResponseDto());
        mockMvc.perform(post(PROJECTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(this.getProjectRequestDto())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(this.getProjectResponseDto().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.client").value(this.getProjectResponseDto().getClient()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(this.getProjectResponseDto().getDescription()))
                .andExpect(status().isOk());

        verify(projectService, times(1)).createProject(this.getProjectRequestDto());
    }

    @Test
    public void updateProjectsTest() throws Exception {
        when(projectService.updateProject(any(String.class), any(ProjectRequestDto.class))).thenReturn(this.getProjectResponseDto());
        mockMvc.perform(put(PROJECTS_URL + "/" + this.getProjectRequestDto().getExternalId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(this.getProjectResponseDto())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(this.getProjectResponseDto().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.client").value(this.getProjectResponseDto().getClient()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(this.getProjectResponseDto().getDescription()))
                .andExpect(status().isOk());

        verify(projectService, times(1)).updateProject(this.getProjectRequestDto().getExternalId(), this.getProjectRequestDto());
    }

    @Test
    public void deleteProjectTest() throws Exception {
        ProjectRequestDto projectRequestDto = this.getProjectRequestDto();
        ProjectResponseDto projectResponseDto = this.getProjectResponseDto();

        ResultActions result = mockMvc.perform(delete(PROJECTS_URL + "/" + projectRequestDto.getExternalId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andExpect(status().isOk());
        verify(projectService, times(1)).deleteProject(projectRequestDto.getExternalId());
    }
}