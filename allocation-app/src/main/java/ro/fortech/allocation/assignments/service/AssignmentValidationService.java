package ro.fortech.allocation.assignments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.fortech.allocation.assignments.dto.AssignmentRequestDto;
import ro.fortech.allocation.assignments.exception.EmployeeAlreadyAssignedToProjectException;
import ro.fortech.allocation.assignments.exception.EmployeeNotAvailableException;
import ro.fortech.allocation.assignments.exception.PeriodNotValidException;
import ro.fortech.allocation.assignments.model.Assignment;
import ro.fortech.allocation.assignments.repository.AssignmentRepository;
import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employees.repository.EmployeeRepository;
import ro.fortech.allocation.employees.service.EmployeeService;
import ro.fortech.allocation.project.dto.ProjectResponseDto;
import ro.fortech.allocation.project.exception.ProjectNotFoundException;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.project.repository.ProjectRepository;
import ro.fortech.allocation.project.service.ProjectService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AssignmentValidationService {
    private final AssignmentRepository assignmentRepository;
    private final ProjectService projectService;
    private final EmployeeService employeeService;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private static final String UNDEFINED_DATE = "9999-12-31";

    public void validateAssignmentForSave(AssignmentRequestDto assignmentDto) {
        isEmployeeAvailable(assignmentDto.getEmployeeUid(), assignmentDto.getAllocationHours(), assignmentDto.getStartDate(), assignmentDto.getEndDate());
        isEmployeeAssignable(assignmentDto.getEmployeeUid(), assignmentDto.getProjectUid(), assignmentDto.getStartDate(), assignmentDto.getEndDate());
        isPeriodValid(assignmentDto.getEmployeeUid(), assignmentDto.getProjectUid(), assignmentDto.getStartDate(), assignmentDto.getEndDate());
    }

    public void validateAssignmentForUpdate(AssignmentRequestDto updatedAssignment, AssignmentRequestDto oldAssignment) {
        if (!updatedAssignment.getProjectUid().equals(oldAssignment.getProjectUid())) {
            isEmployeeAssignable(updatedAssignment.getEmployeeUid(), updatedAssignment.getProjectUid(), updatedAssignment.getStartDate(), updatedAssignment.getEndDate());
        }
        if (updatedAssignment.getAllocationHours() > oldAssignment.getAllocationHours()) {
            isEmployeeAvailable(updatedAssignment.getEmployeeUid(), updatedAssignment.getAllocationHours() - oldAssignment.getAllocationHours(), updatedAssignment.getStartDate(), updatedAssignment.getEndDate());
        }
        isPeriodValid(updatedAssignment.getEmployeeUid(), updatedAssignment.getProjectUid(), updatedAssignment.getStartDate(), updatedAssignment.getEndDate());
    }

    private void isEmployeeAvailable(String uid, Integer requiredHours, Date startDate, Date endDate) {
        Employee employee = employeeRepository.findEmployeeByUid(uid).orElseThrow(() -> new EmployeeNotFoundException(uid));
        List<Assignment> activeAssignmentsList = assignmentRepository.findAssignmentsByEmployee(employee).stream().filter(e -> e.getEndDate().after(startDate) && e.getStartDate().before(endDate)).sorted(Comparator.comparing(Assignment::getStartDate, Date::compareTo)).collect(Collectors.toList());
        if(activeAssignmentsList.isEmpty() && employee.getWorkingHours()<requiredHours){
            throw new EmployeeNotAvailableException(uid);
        }
        for (Assignment assignment : activeAssignmentsList) {
            Integer currentWorkingHoursForAssignmentPeriod = getWorkingHoursDuringPeriodOfAssignment(assignment, activeAssignmentsList);
            if (employee.getWorkingHours() - currentWorkingHoursForAssignmentPeriod < requiredHours)
                throw new EmployeeNotAvailableException(uid);
        }

    }

    private void isEmployeeAssignable(String employeeUid, String projectUid, Date startDate, Date endDate) {

        Employee employee = employeeRepository.findEmployeeByUid(employeeUid).orElseThrow(() -> new EmployeeNotFoundException(employeeUid));
        Project project = projectRepository.findProjectByExternalId(projectUid).orElseThrow(() -> new ProjectNotFoundException(projectUid));
        List<Assignment> assignmentListForProject = assignmentRepository.getAllAssignmentsForGivenEmployeeAndProject(employee.getId(), project.getId());
        if (isAssignmentPeriodIntersectingWithOtherAssignmentsOnSameProject(startDate, endDate, assignmentListForProject)) {
            throw new EmployeeAlreadyAssignedToProjectException(employeeUid, projectUid);
        }
    }

    private void isPeriodValid(String employeeUid, String projectUid, Date startDate, Date endDate) {

        EmployeeDto employeeDto = employeeService.findByUid(employeeUid);
        ProjectResponseDto projectDto = projectService.getProjectByExternalId(projectUid);
        isAssignmentStartDateDuringEmployeeWorkingPeriod(startDate, employeeDto);
        isAssignmentStartDateDuringProjectAllocatedPeriod(startDate, projectDto);

        if (!isAssignmentEndDateInfinite(endDate)) {
            isAssignmentEndDateDuringEmployeeWorkingPeriod(endDate, employeeDto);
            isAssignmentEndDateDuringProjectAllocatedPeriod(endDate, projectDto);
        }
    }

    private Integer getWorkingHoursDuringPeriodOfAssignment(Assignment assignment, List<Assignment> allAssignments) {
        return allAssignments.stream().filter(e -> !assignment.getStartDate().after(e.getStartDate()) && !assignment.getEndDate().before(e.getStartDate())).map(Assignment::getAllocationHours).mapToInt(Integer::valueOf).sum();
    }

    private boolean isAssignmentPeriodIntersectingWithOtherAssignmentsOnSameProject(Date startDate, Date endDate, List<Assignment> assignmentList) {
        for (Assignment assignment : assignmentList) {
            if (!(endDate.before(assignment.getStartDate()) || startDate.after(assignment.getEndDate()))) {
                return true;
            }
        }
        return false;
    }


    private boolean isAssignmentEndDateInfinite(Date endDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return UNDEFINED_DATE.equals(dateFormat.format(endDate));
    }

    private void isAssignmentStartDateDuringEmployeeWorkingPeriod(Date assignmentStartDate, EmployeeDto employeeDto) {
        if (!assignmentStartDate.after(employeeDto.getStartDate())) {
            throw new PeriodNotValidException("Assignment starts before the Employee's contract starts");
        }
        if (!assignmentStartDate.before(employeeDto.getEndDate())) {
            throw new PeriodNotValidException("Assignment starts after the Employee's contract ends");
        }
    }

    private void isAssignmentStartDateDuringProjectAllocatedPeriod(Date assignmentStartDate, ProjectResponseDto projectResponseDto) {
        if (!assignmentStartDate.after(projectResponseDto.getStartDate())) {
            throw new PeriodNotValidException("Assignment starts before the Project's allocated period starts");
        }
        if (!assignmentStartDate.before(projectResponseDto.getEndDate())) {
            throw new PeriodNotValidException("Assignment starts after the Project's allocated period ends");
        }
    }

    private void isAssignmentEndDateDuringEmployeeWorkingPeriod(Date assignmentEndDate, EmployeeDto employeeDto) {
        if (!assignmentEndDate.after(employeeDto.getStartDate())) {
            throw new PeriodNotValidException("Assignment ends before the Employee's contract starts");
        }
        if (!assignmentEndDate.before(employeeDto.getEndDate())) {
            throw new PeriodNotValidException("Assignment ends after the Employee's contract ends");
        }
    }

    private void isAssignmentEndDateDuringProjectAllocatedPeriod(Date assignmentEndDate, ProjectResponseDto projectResponseDto) {
        if (!assignmentEndDate.after(projectResponseDto.getStartDate())) {
            throw new PeriodNotValidException("Assignment ends before the Project's allocated period starts");
        }
        if (!assignmentEndDate.before(projectResponseDto.getEndDate())) {
            throw new PeriodNotValidException("Assignment ends after the Project's allocated period ends");
        }
    }
}
