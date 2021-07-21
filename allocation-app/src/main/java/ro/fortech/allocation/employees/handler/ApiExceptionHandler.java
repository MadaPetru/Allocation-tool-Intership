package ro.fortech.allocation.employees.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {EmployeeNotFoundException.class})
    protected void handleException(EmployeeNotFoundException ex, HttpServletResponse response) throws IOException {
        //log.warn(ex.getLocalizedMessage());
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

}
