package ro.fortech.allocation.technology.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ro.fortech.allocation.technology.exception.TechnologyAlreadyExistsInTheDatabase;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@EnableWebMvc
public class TechnologyApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {TechnologyNotFoundByExternalIdException.class})
    protected ResponseEntity<Object> handleTechnologyNotFoundByExternalIdException(TechnologyNotFoundByExternalIdException ex) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({TechnologyAlreadyExistsInTheDatabase.class})
    protected ResponseEntity<Object> handleTechnologyAlreadyExist(TechnologyAlreadyExistsInTheDatabase ex) {
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}