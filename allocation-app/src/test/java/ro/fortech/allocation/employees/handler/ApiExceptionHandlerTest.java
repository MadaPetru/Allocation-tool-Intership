package ro.fortech.allocation.employees.handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;
import ro.fortech.allocation.project.api.ProjectApiController;
import ro.fortech.allocation.project.service.ProjectService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ApiExceptionHandlerTest {
    @Autowired
    private LocalValidatorFactoryBean validator;

    private final ApiExceptionHandler exceptionHandler = new ApiExceptionHandler();

    @Test
    public void handleConstraintViolationException_givenConstraintViolation_expectBadRequestStatus() {
        EmployeeDto nonValidEmployeeDto = makeNonValidDto();
        Set<ConstraintViolation<EmployeeDto>> constraintViolationSet = validator.validateProperty(nonValidEmployeeDto, "name");

        ConstraintViolationException constraintViolationException = new ConstraintViolationException(constraintViolationSet);

        ResponseEntity<Object> responseEntity = exceptionHandler.handleConstraintViolationException(constraintViolationException);
        assertEquals("name: must not be empty", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void handleEmployeeNotFoundException_givenEmployeeNotFoundException_expectNotFoundStatus() {
        EmployeeNotFoundException ex = new EmployeeNotFoundException("1");
        ResponseEntity<Object> response = exceptionHandler.handleEmployeeNotFoundException(ex);
        assertEquals("Could not find employee with Uid: 1", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    private EmployeeDto makeNonValidDto() {
        return EmployeeDto.builder()
                .email("")
                .name("")
                .internalPosition("")
                .workingHours(-1)
                .build();
    }
}