package ro.fortech.allocation.projecttechnologystack.handler;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ro.fortech.allocation.project.exception.ProjectNotFoundException;
import ro.fortech.allocation.projecttechnologystack.exception.ProjectTechnologyStackAlreadyExistException;
import ro.fortech.allocation.projecttechnologystack.exception.ProjectTechnologyStackNotFoundException;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@EnableWebMvc
public class ProjectTechnologyApiExceptionHandler {
    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ProjectTechnologyStackAlreadyExistException.class})
    protected ResponseEntity<Object> handleProjectTechnologyAlreadyExistException(ProjectTechnologyStackAlreadyExistException ex) {
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {TechnologyNotFoundByExternalIdException.class})
    protected ResponseEntity<Object> handleTechnologyNotFoundByExternalIdException(TechnologyNotFoundByExternalIdException ex) {
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ProjectNotFoundException.class})
    protected ResponseEntity<Object> handleProjectNotFound(ProjectNotFoundException ex) {
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ProjectTechnologyStackNotFoundException.class})
    protected ResponseEntity<Object> handleProjectTechnologyStackNotFoundException(ProjectTechnologyStackNotFoundException ex) {
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}