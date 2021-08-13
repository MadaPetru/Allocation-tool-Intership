package ro.fortech.allocation.assignments.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ro.fortech.allocation.assignments.exception.AssignmentNotFoundException;
import ro.fortech.allocation.assignments.exception.EmployeeAlreadyAssignedToProjectException;
import ro.fortech.allocation.assignments.exception.EmployeeNotAvailableException;
import ro.fortech.allocation.assignments.exception.PeriodNotValidException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@EnableWebMvc
public class AssignmentsApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EmployeeAlreadyAssignedToProjectException.class})
    protected ResponseEntity<Object> handleEmployeeAlreadyAssignedToProjectException(EmployeeAlreadyAssignedToProjectException ex) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EmployeeNotAvailableException.class})
    protected ResponseEntity<Object> handleEmployeeNotAvailableException(EmployeeNotAvailableException ex) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {PeriodNotValidException.class})
    protected ResponseEntity<Object> handlePeriodNotValidException(PeriodNotValidException ex) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AssignmentNotFoundException.class})
    protected ResponseEntity<Object> handleAssignmentNotFoundException(
            AssignmentNotFoundException ex) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

}
