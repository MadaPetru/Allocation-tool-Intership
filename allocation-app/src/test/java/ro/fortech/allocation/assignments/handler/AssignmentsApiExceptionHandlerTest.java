package ro.fortech.allocation.assignments.handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ro.fortech.allocation.assignments.dto.AssignmentRequestDto;
import ro.fortech.allocation.assignments.exception.AssignmentNotFoundException;
import ro.fortech.allocation.assignments.exception.EmployeeAlreadyAssignedToProjectException;
import ro.fortech.allocation.assignments.exception.EmployeeNotAvailableException;
import ro.fortech.allocation.assignments.exception.PeriodNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AssignmentsApiExceptionHandlerTest {

    private final AssignmentsApiExceptionHandler exceptionHandler = new AssignmentsApiExceptionHandler();

    @Autowired
    private LocalValidatorFactoryBean validator;




    @Test
    public void handleConstraintViolationException_givenConstraintViolation_expectBadRequestStatus() {
        AssignmentRequestDto nonValidAssignmentDto = makeNonValidDto();
        Set<ConstraintViolation<AssignmentRequestDto>> constraintViolationSet = validator.validateProperty(nonValidAssignmentDto, "projectUid");

        ConstraintViolationException constraintViolationException = new ConstraintViolationException(constraintViolationSet);

        ResponseEntity<Object> responseEntity = exceptionHandler.handleConstraintViolationException(constraintViolationException);
        assertEquals("projectUid: must not be empty", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void handleEmployeeAlreadyAssignedToProjectException_givenEmployeeAlreadyAssignedToProjectException_expectBadRequestStatus() {
        EmployeeAlreadyAssignedToProjectException ex = new EmployeeAlreadyAssignedToProjectException("1", "1");
        ResponseEntity<Object> response = exceptionHandler.handleEmployeeAlreadyAssignedToProjectException(ex);
        assertEquals("Employee with Uid: " + 1 + "is already assigned, in that period, to project with uid: " + 1, response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void handleEmployeeNotAvailableException_givenEmployeeNotAvailableException_expectBadRequestStatus() {
        EmployeeNotAvailableException ex = new EmployeeNotAvailableException("1");
        ResponseEntity<Object> response = exceptionHandler.handleEmployeeNotAvailableException(ex);
        assertEquals("Employee with Uid: " + 1 + " would exceed his allocated working hours for that period of time.", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void handlePeriodNotValidException_givenPeriodNotValidException_expectBadRequestStatus() {
        PeriodNotValidException ex = new PeriodNotValidException("Start Date not valid");
        ResponseEntity<Object> response = exceptionHandler.handlePeriodNotValidException(ex);
        assertEquals("Start Date not valid", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void handleAssignmentNotFoundException_givenAssignmentNotFoundException_expectNotFoundStatus() {
        AssignmentNotFoundException ex = new AssignmentNotFoundException("1");
        ResponseEntity<Object> response = exceptionHandler.handleAssignmentNotFoundException(ex);
        assertEquals("Could not find assignment with Uid: 1", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private AssignmentRequestDto makeNonValidDto() {
        return AssignmentRequestDto.builder()
                .projectUid("")
                .employeeUid("")
                .projectPosition("")
                .allocationHours(-1.2)
                .build();
    }
}