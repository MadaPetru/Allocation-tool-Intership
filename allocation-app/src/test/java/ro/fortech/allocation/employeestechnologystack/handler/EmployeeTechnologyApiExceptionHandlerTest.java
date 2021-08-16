package ro.fortech.allocation.employeestechnologystack.handler;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;
import ro.fortech.allocation.employeestechnologystack.dto.EmployeeTechnologyStackDto;
import ro.fortech.allocation.employeestechnologystack.exception.EmployeeTechnologyNotFound;
import ro.fortech.allocation.employeestechnologystack.exception.EmployeeTechnologyStackAlreadyExist;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
class EmployeeTechnologyApiExceptionHandlerTest {
    @Autowired
    private LocalValidatorFactoryBean validator;

    private final EmployeeTechnologyApiExceptionHandler exceptionHandler = new EmployeeTechnologyApiExceptionHandler();

    @Test
    void handleConstraintsViolationException_givenExternalsId_expectBadRequest() {
        EmployeeTechnologyStackDto nonValidDto = makeNonValidDto();
        Set<ConstraintViolation<EmployeeTechnologyStackDto>> constraintViolationSet
                = validator.validateProperty(nonValidDto, "technologyExternalId");

        ConstraintViolationException constraintViolationException = new ConstraintViolationException(constraintViolationSet);
        ResponseEntity<Object> responseEntity = exceptionHandler.handleConstraintViolationException(constraintViolationException);
        assertEquals("technologyExternalId: name can not be empty", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void handleEmployeeTechnologyStackAlreadyExistException_givenExternalsId_expectBadRequest() {
        EmployeeTechnologyStackAlreadyExist ex = new EmployeeTechnologyStackAlreadyExist();
        ResponseEntity<Object> response = exceptionHandler.handleEmployeeTechnologyAlreadyExistException(ex);
        assertEquals("Already exist", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleTechnologyNotFoundByExternalIdException_givenExternalsId_expectBadRequest() {
        TechnologyNotFoundByExternalIdException ex = new TechnologyNotFoundByExternalIdException("s");
        ResponseEntity<Object> response = exceptionHandler.handleTechnologyNotFoundByExternalIdException(ex);
        assertEquals("Could not find technology with id: s", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleEmployeeNotFoundByExternalIdException_givenExternalsId_expectBadRequest() {
        EmployeeNotFoundException ex = new EmployeeNotFoundException("ceva");
        ResponseEntity<Object> response = exceptionHandler.handleEmployeeNotFound(ex);
        assertEquals("Could not find employee with Uid: ceva", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleTechnologyEmployeeNotFoundByExternalIdException_givenExternalsId_expectBadRequest() {
        EmployeeTechnologyNotFound ex = new EmployeeTechnologyNotFound();
        ResponseEntity<Object> response = exceptionHandler.handleEmployeeTechnologyStackNotFoundException(ex);
        assertEquals("Not found", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    private EmployeeTechnologyStackDto makeNonValidDto() {
        EmployeeTechnologyStackDto dto = EmployeeTechnologyStackDto.builder()
                .employeeUid("")
                .technologyExternalId("")
                .build();
        return dto;
    }
}