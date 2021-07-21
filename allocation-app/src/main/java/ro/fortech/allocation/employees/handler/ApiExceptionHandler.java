package ro.fortech.allocation.employees.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

@ControllerAdvice
@EnableWebMvc
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(value = {EmployeeNotFoundException.class})
//    protected void handleException(EmployeeNotFoundException ex, HttpServletResponse response) throws IOException {
//        //log.warn(ex.getLocalizedMessage());
//        response.sendError(HttpStatus.NOT_FOUND.value());
//    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) throws IOException {
       // log.warn(ex.getLocalizedMessage());
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(),  HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({ EmployeeNotFoundException.class })
    public ResponseEntity<Object> handleEmployeeNotFoundException(
            EmployeeNotFoundException ex) {
        System.out.println("EXCEPTIE");
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND );
    }
//    @ExceptionHandler(value = {IllegalStateException.class})
//    protected void handleException(IllegalStateException ex, HttpServletResponse response) throws IOException {
//        log.error(ex.getLocalizedMessage(), ex);
//        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
//    }

}
