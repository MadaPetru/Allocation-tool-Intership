package ro.fortech.allocation.assignments.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import ro.fortech.allocation.assignments.dto.AssignmentRequestDto;
import ro.fortech.allocation.assignments.dto.AssignmentResponseDto;
import ro.fortech.allocation.assignments.exception.AssignmentNotFoundException;
import ro.fortech.allocation.assignments.model.Assignment;
import ro.fortech.allocation.assignments.repository.AssignmentRepository;

import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employees.repository.EmployeeRepository;
import ro.fortech.allocation.project.exception.ProjectNotFoundException;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.project.repository.ProjectRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AssignmentServiceTest {

    @InjectMocks
    private AssignmentService assignmentService;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private AssignmentValidationService assignmentValidationService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void save_givenAssignment_expectTheAssignment() throws ParseException {
        Employee employee = new Employee();
        EmployeeDto employeeDto = new EmployeeDto();
        Project project = new Project();

        employee.setUid("123");
        project.setExternalId("123");

        AssignmentRequestDto assignmentDto = AssignmentRequestDto.builder()
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2001-11-11"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2010-11-11"))
                .projectPosition("junior")
                .allocationHours(6.0)
                .employeeUid("123")
                .projectUid("123")
                .build();

        AssignmentResponseDto assignmentResponseDto = AssignmentResponseDto.builder()
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2001-11-11"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2010-11-11"))
                .projectPosition("junior")
                .employeeDto(employeeDto)
                .allocationHours(6.0)
                .projectUid("123")
                .build();

        Assignment assignment = Assignment.builder()
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2001-11-11"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2010-11-11"))
                .projectPosition("junior")
                .employee(employee)
                .project(project)
                .allocationHours(6.0)
                .build();

        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(Optional.of(employee));
        when(projectRepository.findProjectByExternalId(anyString())).thenReturn(Optional.of(project));
        when(modelMapper.map(assignmentDto, Assignment.class)).thenReturn(assignment);
        when(modelMapper.map(assignment, AssignmentResponseDto.class)).thenReturn(assignmentResponseDto);
        when(modelMapper.map(employee, EmployeeDto.class)).thenReturn(employeeDto);
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        assignmentService.save(assignmentDto);

        verify(assignmentRepository).save(any(Assignment.class));
    }

    @Test
    public void update_givenUidAndUpdatedValues_expectTheUpdatedAssignment() throws ParseException {
        Employee employee = new Employee();
        EmployeeDto employeeDto = new EmployeeDto();
        Project project = new Project();

        employee.setUid("123");
        project.setExternalId("123");

        AssignmentRequestDto assignmentDto = AssignmentRequestDto.builder()
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2001-11-11"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2010-11-11"))
                .projectPosition("junior")
                .allocationHours(6.2)
                .employeeUid("123")
                .projectUid("123")
                .uid("1234")
                .build();

        AssignmentResponseDto assignmentResponseDto = AssignmentResponseDto.builder()
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2001-11-11"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2010-11-11"))
                .projectPosition("junior")
                .employeeDto(employeeDto)
                .allocationHours(6.0)
                .projectUid("123")
                .build();

        Assignment assignment = Assignment.builder()
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2001-11-11"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2010-11-11"))
                .projectPosition("junior")
                .employee(employee)
                .project(project)
                .allocationHours(6.2)
                .uid("1234")
                .build();

        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(Optional.of(employee));
        when(projectRepository.findProjectByExternalId(anyString())).thenReturn(Optional.of(project));
        when(assignmentRepository.findAssignmentByUid(anyString())).thenReturn(Optional.of(assignment));
        when(modelMapper.map(assignmentDto, Assignment.class)).thenReturn(assignment);
        when(modelMapper.map(assignment, AssignmentResponseDto.class)).thenReturn(assignmentResponseDto);
        when(modelMapper.map(employee, EmployeeDto.class)).thenReturn(employeeDto);
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        assignmentService.update(assignmentDto, "1234");

        verify(assignmentRepository).save(any(Assignment.class));
    }


    @Test
    public void deleteByUid_givenUid_expectToSetTheEndDate() throws ParseException {
        Employee employee = new Employee();
        Project project = new Project();

        employee.setUid("123");
        project.setExternalId("123");

        Assignment assignment = Assignment.builder()
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2001-11-11"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2010-11-11"))
                .projectPosition("junior")
                .employee(employee)
                .project(project)
                .allocationHours(6.2)
                .uid("1234")
                .build();


        when(assignmentRepository.findAssignmentByUid(anyString())).thenReturn(Optional.of(assignment));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        assignmentService.deleteByUid("1234");
        Date todayDate = java.util.Calendar.getInstance().getTime();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        verify(assignmentRepository).save(any(Assignment.class));
        assertEquals(formatter.format(todayDate), formatter.format(assignment.getEndDate()));
    }

    @Test
    public void validateUpdate_givenAssignment_expectOk() throws ParseException {
        Employee employee = new Employee();
        Project project = new Project();

        employee.setUid("123");
        project.setExternalId("123");
        AssignmentRequestDto assignmentDto = AssignmentRequestDto.builder()
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2001-11-11"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2010-11-11"))
                .projectPosition("junior")
                .allocationHours(6.2)
                .employeeUid("123")
                .projectUid("123")
                .uid("1234")
                .build();

        Assignment assignment = Assignment.builder()
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2001-11-11"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2010-11-11"))
                .projectPosition("junior")
                .employee(employee)
                .project(project)
                .allocationHours(6.2)
                .uid("1234")
                .build();
        when(assignmentRepository.findAssignmentByUid(anyString())).thenReturn(Optional.of(assignment));
        when(modelMapper.map(assignment, AssignmentRequestDto.class)).thenReturn(assignmentDto);
        doNothing().when(assignmentValidationService).validateAssignmentForUpdate(assignmentDto, assignmentDto);

        assignmentService.validate(assignmentDto);

        verify(assignmentRepository).findAssignmentByUid(assignment.getUid());
        verify(assignmentValidationService).validateAssignmentForUpdate(assignmentDto, assignmentDto);

    }

    @Test
    public void validateSave_givenAssignment_expectOk() throws ParseException {
        Employee employee = new Employee();
        Project project = new Project();

        employee.setUid("123");
        project.setExternalId("123");
        AssignmentRequestDto assignmentDto = AssignmentRequestDto.builder()
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2001-11-11"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2010-11-11"))
                .projectPosition("junior")
                .allocationHours(6.2)
                .employeeUid("123")
                .projectUid("123")
                .uid("1234")
                .build();
        when(assignmentRepository.findAssignmentByUid(anyString())).thenReturn(Optional.ofNullable(null));
        doNothing().when(assignmentValidationService).validateAssignmentForSave(assignmentDto);

        assignmentService.validate(assignmentDto);

        verify(assignmentValidationService).validateAssignmentForSave(assignmentDto);

    }
    @Test
    public void findAssignmentsByProject_givenProject_expectAssignments(){
        String projectUid = "123";
        Project project = Project.builder().externalId(projectUid).build();
        Employee employee = new Employee();
        EmployeeDto employeeDto = new EmployeeDto();
        List<Assignment> repositoryResponse = new ArrayList<>();
        Assignment assignment = Assignment.builder().employee(employee).project(project).build();
        repositoryResponse.add(assignment);
        AssignmentResponseDto assignmentResponseDto = new AssignmentResponseDto();

        when(projectRepository.findProjectByExternalId(projectUid)).thenReturn(Optional.of(project));
        when(assignmentRepository.findAssignmentsByProject(project)).thenReturn(repositoryResponse);
        when(modelMapper.map(assignment,AssignmentResponseDto.class)).thenReturn(assignmentResponseDto);
        when(modelMapper.map(employee,EmployeeDto.class)).thenReturn(employeeDto);
        assignmentService.findAssignmentsByProject(projectUid);

        verify(projectRepository).findProjectByExternalId(projectUid);
        verify(assignmentRepository).findAssignmentsByProject(project);
        verify(modelMapper).map(assignment,AssignmentResponseDto.class);
    }
    @Test(expected = ProjectNotFoundException.class)
    public void findAssignmentsByProject_givenProject_expectProjectNotFoundException(){
        String projectUid = "123";
        when(projectRepository.findProjectByExternalId(projectUid)).thenReturn(Optional.ofNullable(null));
        assignmentService.findAssignmentsByProject(projectUid);

        verify(projectRepository).findProjectByExternalId(projectUid);
    }

    @Test(expected = AssignmentNotFoundException.class)
    public void update_givenUidAndUpdatedValues_expectAssignmentNotFoundException() throws ParseException {
        AssignmentRequestDto assignmentDto = AssignmentRequestDto.builder()
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2001-11-11"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2010-11-11"))
                .projectPosition("junior")
                .allocationHours(6.2)
                .employeeUid("123")
                .projectUid("123")
                .uid("1234")
                .build();
        when(assignmentRepository.findAssignmentByUid(anyString())).thenReturn(Optional.empty());
        assignmentService.update(assignmentDto, "1");
    }

    @Test(expected = AssignmentNotFoundException.class)
    public void delete_givenUidAndUpdatedValues_expectAssignmentNotFoundException() {

        when(assignmentRepository.findAssignmentByUid(anyString())).thenReturn(Optional.empty());
        assignmentService.deleteByUid("1");
    }


}