package ro.fortech.allocation.project.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import java.util.TimeZone;
import static org.junit.Assert.*;
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
    public void setup(){
        TimeZone defaultTimeZone = TimeZone.getTimeZone("UTC");
        TimeZone.setDefault(defaultTimeZone);
    }

    @MockBean
    private ProjectService projectService;

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String PROJECTS_URL = "/projects";

    ProjectRequestDto projectRequestDto = this.getProjectRequestDto();
    ProjectResponseDto projectResponseDto = this.getProjectResponseDto();

    @Test
    public void getProjectByExternalIdTest() throws Exception {
        when(projectService.getProjectByExternalId(this.getProject().getExternalId())).thenReturn(this.getProjectResponseDto());
        mockMvc.perform(get(PROJECTS_URL + "/" + this.getProject().getExternalId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(this.getProjectResponseDto().getName()));
        verify(projectService, times(1)).getProjectByExternalId(this.getProject().getExternalId());
        assertEquals(1,1);
    }

    @Test
    public void createProject() throws Exception {
        when(projectService.createProject(any(ProjectRequestDto.class))).thenReturn(projectResponseDto);
        mockMvc.perform(post(PROJECTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andExpect(status().isOk());

        verify(projectService, times(1)).createProject(projectRequestDto);
    }

//    @Test
//    public void updateProjects() throws Exception {
//        projectRequestDto.setName("Name");
//        projectResponseDto.setName("Name");
//
//        when(projectService
//                .updateProject(projectRequestDto.getExternalId(), projectRequestDto))
//                .thenReturn(projectResponseDto);
//
//        ResultActions result = mockMvc.perform(put(PROJECTS_URL + "/" + projectRequestDto.getExternalId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(projectRequestDto)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//
//        ResultActions json = result.andExpect(content().json(new ObjectMapper().writeValueAsString(projectResponseDto), true));
//        verify(projectService, times(1)).updateProject(projectRequestDto.getExternalId(), projectRequestDto);
//    }

    @Test
    public void deleteProjectTest() throws Exception {
        ResultActions result = mockMvc.perform(delete(PROJECTS_URL + "/" + projectRequestDto.getExternalId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andExpect(status().isOk());
        verify(projectService,times(1)).deleteProject(projectRequestDto.getExternalId());
    }
}