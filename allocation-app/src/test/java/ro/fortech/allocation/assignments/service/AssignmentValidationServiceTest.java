package ro.fortech.allocation.assignments.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ro.fortech.allocation.assignments.dto.AssignmentRequestDto;
import ro.fortech.allocation.assignments.exception.EmployeeAlreadyAssignedToProjectException;
import ro.fortech.allocation.assignments.exception.EmployeeNotAvailableException;
import ro.fortech.allocation.assignments.exception.PeriodNotValidException;
import ro.fortech.allocation.assignments.model.Assignment;
import ro.fortech.allocation.assignments.repository.AssignmentRepository;
import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employees.repository.EmployeeRepository;
import ro.fortech.allocation.employees.service.EmployeeService;
import ro.fortech.allocation.project.dto.ProjectResponseDto;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.project.repository.ProjectRepository;
import ro.fortech.allocation.project.service.ProjectService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AssignmentValidationServiceTest {
    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private ProjectService projectService;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private AssignmentValidationService assignmentValidationService;

    @Test
    public void validateAssignmentForSave_givenAssignmentDto_expectOk() throws ParseException {
        AssignmentRequestDto assignmentDto = makeDto();
        Employee employee = Employee.builder().id(1L).uid("1").workingHours(8).build();
        Project project = Project.builder().id(1L).externalId("1").build();
        EmployeeDto employeeDto = makeEmployeeDto();
        ProjectResponseDto projectDto = makeProjectDto();
        List<Assignment> assignmentList = new ArrayList<>();
        assignmentList.add(makeAssignment("2020-04-08", "2020-05-08"));
        List<Assignment> workingAssignments = new ArrayList<>();
        workingAssignments.add(makeAssignment("2020-05-10", "2020-06-10"));


        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(java.util.Optional.ofNullable(employee));
        when(assignmentRepository.findAssignmentsByEmployee(any(Employee.class))).thenReturn(workingAssignments);
        when(projectRepository.findProjectByExternalId(anyString())).thenReturn(java.util.Optional.ofNullable(project));
        when(assignmentRepository.getAllAssignmentsForGivenEmployeeAndProject(any(Long.class), any(Long.class))).thenReturn(assignmentList);
        when(employeeService.findByUid(anyString())).thenReturn(employeeDto);
        when(projectService.getProjectByExternalId(anyString())).thenReturn(projectDto);

        assignmentValidationService.validateAssignmentForSave(assignmentDto);

        verify(employeeRepository, times(2)).findEmployeeByUid(assignmentDto.getEmployeeUid());
        verify(employeeService).findByUid(assignmentDto.getEmployeeUid());
        verify(projectRepository).findProjectByExternalId(assignmentDto.getProjectUid());
        verify(projectService).getProjectByExternalId(assignmentDto.getProjectUid());
        verify(assignmentRepository).findAssignmentsByEmployee(employee);
        verify(assignmentRepository).getAllAssignmentsForGivenEmployeeAndProject(employee.getId(), project.getId());
    }


    @Test(expected = EmployeeNotAvailableException.class)
    public void validateAssignmentForSave_givenAssignmentDto_expectEmployeeNotAvailableException() throws ParseException {
        AssignmentRequestDto assignmentDto = makeDto();
        Employee employee = Employee.builder().id(1L).uid("1").workingHours(8).build();
        List<Assignment> workingAssignments = new ArrayList<>();
        workingAssignments.add(makeAssignment("2020-05-10", "2020-06-10"));
        workingAssignments.add(makeAssignment("2020-05-10", "2020-08-10"));

        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(java.util.Optional.ofNullable(employee));
        when(assignmentRepository.findAssignmentsByEmployee(any(Employee.class))).thenReturn(workingAssignments);

        assignmentValidationService.validateAssignmentForSave(assignmentDto);
    }

    @Test(expected = EmployeeAlreadyAssignedToProjectException.class)
    public void validateAssignmentForSave_givenAssignmentDto_expectEmployeeAlreadyAssignedToProjectException() throws ParseException {
        AssignmentRequestDto assignmentDto = makeDto();
        Employee employee = Employee.builder().id(1L).uid("1").workingHours(8).build();
        Project project = Project.builder().id(1L).externalId("1").build();
        List<Assignment> workingAssignments = new ArrayList<>();
        workingAssignments.add(makeAssignment("2020-05-08", "2020-06-08"));
        List<Assignment> assignmentList = new ArrayList<>();
        assignmentList.add(makeAssignment("2020-05-08", "2020-05-30"));

        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(java.util.Optional.ofNullable(employee));
        when(assignmentRepository.findAssignmentsByEmployee(any(Employee.class))).thenReturn(workingAssignments);
        when(projectRepository.findProjectByExternalId(anyString())).thenReturn(java.util.Optional.ofNullable(project));
        when(assignmentRepository.getAllAssignmentsForGivenEmployeeAndProject(any(Long.class), any(Long.class))).thenReturn(assignmentList);

        assignmentValidationService.validateAssignmentForSave(assignmentDto);
    }

    @Test(expected = PeriodNotValidException.class)
    public void validateAssignmentForSave_givenAssignmentDto_expectPeriodNotValidException() throws ParseException {
        AssignmentRequestDto assignmentDto = makeNonValidDto();
        Employee employee = Employee.builder().id(1L).uid("1").workingHours(8).build();
        Project project = Project.builder().id(1L).externalId("1").build();
        EmployeeDto employeeDto = makeEmployeeDto();
        ProjectResponseDto projectDto = makeProjectDto();
        List<Assignment> assignmentList = new ArrayList<>();
        assignmentList.add(makeAssignment("2017-04-08", "2017-05-08"));
        List<Assignment> workingAssignments = new ArrayList<>();
        workingAssignments.add(makeAssignment("2020-05-10", "2020-06-10"));

        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(java.util.Optional.ofNullable(employee));
        when(assignmentRepository.findAssignmentsByEmployee(any(Employee.class))).thenReturn(workingAssignments);

        when(projectRepository.findProjectByExternalId(anyString())).thenReturn(java.util.Optional.ofNullable(project));
        when(assignmentRepository.getAllAssignmentsForGivenEmployeeAndProject(any(Long.class), any(Long.class))).thenReturn(assignmentList);
        when(employeeService.findByUid(anyString())).thenReturn(employeeDto);
        when(projectService.getProjectByExternalId(anyString())).thenReturn(projectDto);

        assignmentValidationService.validateAssignmentForSave(assignmentDto);
    }


    @Test
    public void validateAssignmentForUpdate_givenUpdatedAssignmentDto_expectOk() throws ParseException {
        AssignmentRequestDto oldAssignmentDto = makeDto();
        Employee employee = Employee.builder().id(1L).uid("1").workingHours(6).build();
        Project project = Project.builder().id(1L).externalId("1").build();
        EmployeeDto employeeDto = makeEmployeeDto();
        ProjectResponseDto projectDto = makeProjectDto();
        List<Assignment> assignmentList = new ArrayList<>();
        assignmentList.add(makeAssignment("2020-04-08", "2020-05-08"));
        List<Assignment> workingAssignments = new ArrayList<>();
        workingAssignments.add(makeAssignment("2020-05-10", "2020-06-10"));

        AssignmentRequestDto updatedAssignmentDto = makeDto();
        updatedAssignmentDto.setProjectUid("99");
        updatedAssignmentDto.setAllocationHours(6);

        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(Optional.of(employee));
        when(assignmentRepository.findAssignmentsByEmployee(any(Employee.class))).thenReturn(workingAssignments);
        when(projectRepository.findProjectByExternalId(anyString())).thenReturn(java.util.Optional.ofNullable(project));
        when(assignmentRepository.getAllAssignmentsForGivenEmployeeAndProject(any(Long.class), any(Long.class))).thenReturn(assignmentList);
        when(employeeService.findByUid(anyString())).thenReturn(employeeDto);
        when(projectService.getProjectByExternalId(anyString())).thenReturn(projectDto);

        assignmentValidationService.validateAssignmentForUpdate(updatedAssignmentDto, oldAssignmentDto);

        verify(employeeRepository, times(2)).findEmployeeByUid(updatedAssignmentDto.getEmployeeUid());
        verify(employeeService).findByUid(updatedAssignmentDto.getEmployeeUid());
        verify(projectRepository).findProjectByExternalId(updatedAssignmentDto.getProjectUid());
        verify(projectService).getProjectByExternalId(updatedAssignmentDto.getProjectUid());
        verify(assignmentRepository).findAssignmentsByEmployee(employee);
        verify(assignmentRepository).getAllAssignmentsForGivenEmployeeAndProject(employee.getId(), project.getId());

    }

    @Test
    public void validateAssignmentForUpdate_givenAssignmentDtoSameProject_expectOk() throws ParseException {
        AssignmentRequestDto oldAssignmentDto = makeDto();
        EmployeeDto employeeDto = makeEmployeeDto();
        ProjectResponseDto projectDto = makeProjectDto();
        AssignmentRequestDto updatedAssignmentDto = makeDto();
        updatedAssignmentDto.setAllocationHours(3);

        when(employeeService.findByUid(anyString())).thenReturn(employeeDto);
        when(projectService.getProjectByExternalId(anyString())).thenReturn(projectDto);

        assignmentValidationService.validateAssignmentForUpdate(updatedAssignmentDto, oldAssignmentDto);

        verify(employeeService).findByUid(updatedAssignmentDto.getEmployeeUid());
        verify(projectService).getProjectByExternalId(updatedAssignmentDto.getProjectUid());
    }

    @Test(expected = EmployeeNotAvailableException.class)
    public void validateAssignmentForUpdate_givenAssignmentDto_expectEmployeeNotAvailableException() throws ParseException {
        AssignmentRequestDto oldAssignmentDto = makeDto();
        Employee employee = Employee.builder().id(1L).uid("1").workingHours(8).build();
        Project project = Project.builder().id(1L).externalId("1").build();
        List<Assignment> assignmentList = new ArrayList<>();
        assignmentList.add(makeAssignment("2020-05-10", "2020-06-10"));
        assignmentList.add(makeAssignment("2020-04-08", "2020-06-08"));

        AssignmentRequestDto updatedAssignmentDto = makeDto();
        updatedAssignmentDto.setProjectUid("99");
        updatedAssignmentDto.setAllocationHours(6);
        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(java.util.Optional.of(employee));
        when(assignmentRepository.findAssignmentsByEmployee(any(Employee.class))).thenReturn(assignmentList);
        when(projectRepository.findProjectByExternalId(anyString())).thenReturn(Optional.of(project));
        when(assignmentRepository.getAllAssignmentsForGivenEmployeeAndProject(any(Long.class), any(Long.class))).thenReturn(new ArrayList<>());

        assignmentValidationService.validateAssignmentForUpdate(updatedAssignmentDto, oldAssignmentDto);
    }

    @Test(expected = EmployeeAlreadyAssignedToProjectException.class)
    public void validateAssignmentForUpdate_givenAssignmentDto_expectEmployeeAlreadyAssignedToProjectException() throws ParseException {
        AssignmentRequestDto oldAssignmentDto = makeDto();
        Employee employee = Employee.builder().id(1L).uid("1").workingHours(8).build();
        Project project = Project.builder().id(1L).externalId("1").build();
        List<Assignment> assignmentList = new ArrayList<>();
        assignmentList.add(makeAssignment("2020-05-08", "2020-06-08"));

        AssignmentRequestDto updatedAssignmentDto = makeDto();
        updatedAssignmentDto.setProjectUid("99");


        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(java.util.Optional.ofNullable(employee));
        when(projectRepository.findProjectByExternalId(anyString())).thenReturn(java.util.Optional.ofNullable(project));
        when(assignmentRepository.getAllAssignmentsForGivenEmployeeAndProject(any(Long.class), any(Long.class))).thenReturn(assignmentList);

        assignmentValidationService.validateAssignmentForUpdate(updatedAssignmentDto, oldAssignmentDto);
    }

    @Test(expected = PeriodNotValidException.class)
    public void validateAssignmentForUpdate_givenAssignmentDto_expectPeriodNotValidException() throws ParseException {
        AssignmentRequestDto oldAssignmentDto = makeDto();
        Employee employee = Employee.builder().id(1L).uid("1").workingHours(8).build();
        Project project = Project.builder().id(1L).externalId("1").build();
        EmployeeDto employeeDto = makeEmployeeDto();
        ProjectResponseDto projectDto = makeProjectDto();
        List<Assignment> assignmentList = new ArrayList<>();
        assignmentList.add(makeAssignment("2017-04-08", "2017-05-08"));

        AssignmentRequestDto updatedAssignmentDto = makeNonValidDto();
        updatedAssignmentDto.setProjectUid("99");
        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(java.util.Optional.ofNullable(employee));

        when(projectRepository.findProjectByExternalId(anyString())).thenReturn(java.util.Optional.ofNullable(project));
        when(assignmentRepository.getAllAssignmentsForGivenEmployeeAndProject(any(Long.class), any(Long.class))).thenReturn(assignmentList);
        when(employeeService.findByUid(anyString())).thenReturn(employeeDto);
        when(projectService.getProjectByExternalId(anyString())).thenReturn(projectDto);

        assignmentValidationService.validateAssignmentForUpdate(updatedAssignmentDto, oldAssignmentDto);
    }


    private AssignmentRequestDto makeDto() throws ParseException {
        return AssignmentRequestDto.builder()
                .uid("22")
                .projectUid("1")
                .employeeUid("1")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .projectPosition("junior")
                .allocationHours(4)
                .build();
    }

    private Assignment makeAssignment(String startDate, String endDate) throws ParseException {
        return Assignment.builder()
                .uid("22")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse(startDate))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse(endDate))
                .projectPosition("junior")
                .allocationHours(4)
                .build();
    }

    private AssignmentRequestDto makeNonValidDto() throws ParseException {
        return AssignmentRequestDto.builder()
                .uid("22")
                .projectUid("1")
                .employeeUid("1")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("9999-12-31"))
                .projectPosition("junior")
                .allocationHours(4)
                .build();
    }

    private EmployeeDto makeEmployeeDto() throws ParseException {
        return EmployeeDto.builder()
                .uid("1")
                .workingHours(6)
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("9999-12-31"))
                .build();
    }

    private ProjectResponseDto makeProjectDto() throws ParseException {
        return ProjectResponseDto.builder()
                .externalId("1")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-04-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("9999-12-31"))
                .build();
    }

}