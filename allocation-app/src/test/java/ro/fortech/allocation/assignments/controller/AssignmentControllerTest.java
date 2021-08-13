package ro.fortech.allocation.assignments.controller;

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
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ro.fortech.allocation.assignments.dto.AssignmentRequestDto;
import ro.fortech.allocation.assignments.dto.AssignmentResponseDto;
import ro.fortech.allocation.assignments.service.AssignmentService;

import javax.validation.ConstraintViolation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@EnableSpringDataWebSupport
public class AssignmentControllerTest {

    @MockBean
    private AssignmentService assignmentService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocalValidatorFactoryBean validator;

    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        TimeZone defaultTimeZone = TimeZone.getTimeZone("UTC");
        TimeZone.setDefault(defaultTimeZone);
    }

    @Test
    public void addAssignment_givenAssignment_expectTheCreatedAssignment() throws Exception {
        AssignmentResponseDto request = makeResponseDto();
        when(assignmentService.save(Mockito.any(AssignmentRequestDto.class))).thenReturn(request);

        mockMvc.perform(post("/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.projectUid").value(request.getProjectUid()))
                .andExpect(jsonPath("$.projectPosition").value(request.getProjectPosition()))
                .andExpect(jsonPath("$.allocationHours").value(request.getAllocationHours()));

        verify(assignmentService).save(Mockito.any(AssignmentRequestDto.class));
    }

    @Test
    public void deleteEmployee_givenUid_expectStatusOk() throws Exception {
        AssignmentRequestDto assignmentDto = makeDto();
        mockMvc.perform(delete("/assignments/" + assignmentDto.getUid())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(assignmentService).deleteByUid(assignmentDto.getUid());
    }

    @Test
    public void updateAssignment_givenUidAndUpdatedAssignment_expectUpdatedAssignment() throws Exception {
        AssignmentRequestDto assignmentDto = makeDto();
        AssignmentResponseDto assignmentResponseDto = makeResponseDto();
        when(assignmentService.update(Mockito.any(AssignmentRequestDto.class), Mockito.anyString())).thenReturn(assignmentResponseDto);
        mockMvc.perform(put("/assignments/" + assignmentDto.getUid())
                        .content(mapper.writeValueAsString(assignmentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectUid").value(assignmentDto.getProjectUid()))
                .andExpect(jsonPath("$.projectPosition").value(assignmentDto.getProjectPosition()))
                .andExpect(jsonPath("$.allocationHours").value(assignmentDto.getAllocationHours()));

        verify(assignmentService).update(assignmentDto, assignmentDto.getUid());
    }

    @Test
    public void assignmentValidation_givenAssignment_expectOk() throws Exception {
        AssignmentResponseDto request = makeResponseDto();
        doNothing().when(assignmentService).validate(Mockito.any(AssignmentRequestDto.class));

        mockMvc.perform(post("/assignments/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(assignmentService).validate(Mockito.any(AssignmentRequestDto.class));
    }

    @Test
    public void findAssignmentsByProject_givenProject_expectAssignments()throws Exception{
        String projectUid = "123";
        List<AssignmentResponseDto> result = new ArrayList<>();
        when(assignmentService.findAssignmentsByProject(anyString())).thenReturn(result);

        mockMvc.perform(get("/assignments/project/"+projectUid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(projectUid)))
                        .andExpect(status().isOk());
        verify(assignmentService).findAssignmentsByProject(projectUid);
    }

    @Test
    public void validation_givenNonValid_expectConstraintViolation() {
        AssignmentRequestDto nonValidAssignmentRequestDto = makeNonValidDto();
        Set<ConstraintViolation<AssignmentRequestDto>> constraintViolationSet = validator.validateProperty(nonValidAssignmentRequestDto, "employeeUid");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertEquals("must not be empty", constraintViolationSet.iterator().next().getMessage());

        constraintViolationSet = validator.validateProperty(nonValidAssignmentRequestDto, "projectUid");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertEquals("must not be empty", constraintViolationSet.iterator().next().getMessage());

        constraintViolationSet = validator.validateProperty(nonValidAssignmentRequestDto, "allocationHours");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertEquals("must be greater than or equal to 1", constraintViolationSet.iterator().next().getMessage());

        constraintViolationSet = validator.validateProperty(nonValidAssignmentRequestDto, "projectPosition");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertEquals("must not be empty", constraintViolationSet.iterator().next().getMessage());


    }

    private AssignmentRequestDto makeDto() throws ParseException {
        return AssignmentRequestDto.builder()
                .uid("22")
                .projectUid("1")
                .employeeUid("1")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .projectPosition("a")
                .allocationHours(2)
                .build();
    }

    private AssignmentResponseDto makeResponseDto() throws ParseException {
        return AssignmentResponseDto.builder()
                .uid("22")
                .projectUid("1")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .projectPosition("a")
                .allocationHours(2)
                .build();
    }

    private AssignmentRequestDto makeNonValidDto() {
        return AssignmentRequestDto.builder()
                .projectUid("")
                .employeeUid("")
                .projectPosition("")
                .allocationHours(-1)
                .build();
    }
}