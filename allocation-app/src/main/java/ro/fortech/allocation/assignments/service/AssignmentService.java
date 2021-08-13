package ro.fortech.allocation.assignments.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ro.fortech.allocation.assignments.dto.AssignmentRequestDto;
import ro.fortech.allocation.assignments.dto.AssignmentResponseDto;
import ro.fortech.allocation.assignments.exception.AssignmentNotFoundException;
import ro.fortech.allocation.assignments.model.Assignment;
import ro.fortech.allocation.assignments.repository.AssignmentRepository;
import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employees.repository.EmployeeRepository;
import ro.fortech.allocation.project.exception.ProjectNotFoundException;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.project.repository.ProjectRepository;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final AssignmentValidationService assignmentValidationService;
    private static final String UNDEFINED_DATE = "9999-12-31";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public AssignmentResponseDto save(@Valid AssignmentRequestDto assignmentDto) throws ParseException {
        if (assignmentDto.getEndDate() == null) {
            assignmentDto.setEndDate(new SimpleDateFormat(DATE_FORMAT).parse(UNDEFINED_DATE));
        }
        assignmentDto.setUid(UUID.randomUUID().toString());
        return fromEntityToResponseDto(assignmentRepository.save(fromDtoToEntity(assignmentDto)));
    }

    public AssignmentResponseDto update(@Valid AssignmentRequestDto assignmentDto, String uid) throws ParseException {
        Assignment oldAssignment = assignmentRepository.findAssignmentByUid(uid).orElseThrow(() -> new AssignmentNotFoundException(uid));

        if (assignmentDto.getEndDate() == null) {
            assignmentDto.setEndDate(new SimpleDateFormat(DATE_FORMAT).parse(UNDEFINED_DATE));
        }
        Assignment updatedAssignment = fromDtoToEntity(assignmentDto);
        updatedAssignment.setId(oldAssignment.getId());
        updatedAssignment.setUid(oldAssignment.getUid());
        return fromEntityToResponseDto(assignmentRepository.save(updatedAssignment));
    }

    public void deleteByUid(String uid) {
        Assignment assignment = assignmentRepository.findAssignmentByUid(uid).orElseThrow(() -> new AssignmentNotFoundException(uid));
        Date todayDate = java.util.Calendar.getInstance().getTime();
        assignment.setEndDate(todayDate);
        assignmentRepository.save(assignment);
    }

    public void validate(@Valid AssignmentRequestDto assignmentRequestDto) throws ParseException {
        if (assignmentRequestDto.getEndDate() == null) {
            assignmentRequestDto.setEndDate(new SimpleDateFormat(DATE_FORMAT).parse(UNDEFINED_DATE));
        }
        try {
            Assignment oldAssignment = assignmentRepository.findAssignmentByUid(assignmentRequestDto.getUid()).get();
            assignmentValidationService.validateAssignmentForUpdate(assignmentRequestDto, fromEntityToRequestDto(oldAssignment));
        } catch (NoSuchElementException exception) {
            assignmentValidationService.validateAssignmentForSave(assignmentRequestDto);
        }
    }

    public List<AssignmentResponseDto> findAssignmentsByProject(String projectUid){
        Project project = projectRepository.findProjectByExternalId(projectUid).orElseThrow(()->new ProjectNotFoundException(projectUid));
        return assignmentRepository.findAssignmentsByProject(project).stream().map(this::fromEntityToResponseDto).collect(Collectors.toList());
    }

    private Assignment fromDtoToEntity(AssignmentRequestDto assignmentDto) {
        Assignment assignment = modelMapper.map(assignmentDto, Assignment.class);
        assignment.setEmployee(this.getEmployee(assignmentDto));
        assignment.setProject(this.getProject(assignmentDto));
        return assignment;
    }

    private AssignmentResponseDto fromEntityToResponseDto(Assignment assignment) {
        AssignmentResponseDto assignmentResponseDto = modelMapper.map(assignment, AssignmentResponseDto.class);
        assignmentResponseDto.setEmployeeDto(this.fromEntityToDtoEmployee(assignment.getEmployee()));
        assignmentResponseDto.setProjectUid(assignment.getProject().getExternalId());
        return assignmentResponseDto;
    }

    private AssignmentRequestDto fromEntityToRequestDto(Assignment assignment) {
        AssignmentRequestDto assignmentRequestDto = modelMapper.map(assignment, AssignmentRequestDto.class);
        assignmentRequestDto.setEmployeeUid(assignment.getEmployee().getUid());
        assignmentRequestDto.setProjectUid(assignment.getProject().getExternalId());
        return assignmentRequestDto;
    }

    private Project getProject(AssignmentRequestDto assignmentDto) {
        return projectRepository.findProjectByExternalId(assignmentDto.getProjectUid()).orElseThrow(() -> new ProjectNotFoundException(assignmentDto.getProjectUid()));
    }

    private Employee getEmployee(AssignmentRequestDto assignmentDto) {
        return employeeRepository.findEmployeeByUid(assignmentDto.getEmployeeUid()).orElseThrow(() -> new EmployeeNotFoundException(assignmentDto.getEmployeeUid()));
    }

    private EmployeeDto fromEntityToDtoEmployee(Employee employee) {
        return modelMapper.map(employee, EmployeeDto.class);
    }
}
