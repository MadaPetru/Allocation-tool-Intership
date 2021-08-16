package ro.fortech.allocation.employeestechnologystack.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;
import ro.fortech.allocation.employeestechnologystack.exception.EmployeeTechnologyNotFound;
import ro.fortech.allocation.employeestechnologystack.exception.EmployeeTechnologyStackAlreadyExist;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@EnableWebMvc
public class EmployeeTechnologyApiExceptionHandler {
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EmployeeTechnologyStackAlreadyExist.class})
    protected ResponseEntity<Object> handleEmployeeTechnologyAlreadyExistException(EmployeeTechnologyStackAlreadyExist ex) {
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {TechnologyNotFoundByExternalIdException.class})
    protected ResponseEntity<Object> handleTechnologyNotFoundByExternalIdException(TechnologyNotFoundByExternalIdException ex) {
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EmployeeNotFoundException.class})
    protected ResponseEntity<Object> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EmployeeTechnologyNotFound.class})
    protected ResponseEntity<Object> handleEmployeeTechnologyStackNotFoundException(EmployeeTechnologyNotFound ex) {
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
