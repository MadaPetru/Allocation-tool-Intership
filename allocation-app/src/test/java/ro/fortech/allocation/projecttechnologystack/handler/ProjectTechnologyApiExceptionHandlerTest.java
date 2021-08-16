package ro.fortech.allocation.projecttechnologystack.handler;

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
import ro.fortech.allocation.employeestechnologystack.handler.EmployeeTechnologyApiExceptionHandler;
import ro.fortech.allocation.project.exception.ProjectNotFoundException;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.projecttechnologystack.dto.ProjectTechnologyStackDto;
import ro.fortech.allocation.projecttechnologystack.exception.ProjectTechnologyStackAlreadyExistException;
import ro.fortech.allocation.projecttechnologystack.exception.ProjectTechnologyStackNotFoundException;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
class ProjectTechnologyApiExceptionHandlerTest {
    @Autowired
    private LocalValidatorFactoryBean validator;

    private final ProjectTechnologyApiExceptionHandler exceptionHandler = new ProjectTechnologyApiExceptionHandler();

    @Test
    void handleConstraintsViolationException_givenExternalsId_expectBadRequest() {
        ProjectTechnologyStackDto nonValidDto = makeNonValidDto();
        Set<ConstraintViolation<ProjectTechnologyStackDto>> constraintViolationSet
                = validator.validateProperty(nonValidDto, "technologyExternalId");

        ConstraintViolationException constraintViolationException = new ConstraintViolationException(constraintViolationSet);
        ResponseEntity<Object> responseEntity = exceptionHandler.handleConstraintViolationException(constraintViolationException);
        assertEquals("technologyExternalId: name can not be empty", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void handleProjectTechnologyStackAlreadyExistException_givenExternalsId_expectBadRequest() {
        ProjectTechnologyStackAlreadyExistException ex = new ProjectTechnologyStackAlreadyExistException("s");
        ResponseEntity<Object> response = exceptionHandler.handleProjectTechnologyAlreadyExistException(ex);
        assertEquals("s", response.getBody());
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
    void handleProjectNotFoundByExternalIdException_givenExternalsId_expectBadRequest() {
        ProjectNotFoundException ex = new ProjectNotFoundException("ceva");
        ResponseEntity<Object> response = exceptionHandler.handleProjectNotFound(ex);
        assertEquals("Could not find project with externalId: ceva", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleTechnologyProjectNotFoundByExternalIdException_givenExternalsId_expectBadRequest() {
        ProjectTechnologyStackNotFoundException ex = new ProjectTechnologyStackNotFoundException("s s");
        ResponseEntity<Object> response = exceptionHandler.handleProjectTechnologyStackNotFoundException(ex);
        assertEquals("s s", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    private ProjectTechnologyStackDto makeNonValidDto(){
        ProjectTechnologyStackDto dto = ProjectTechnologyStackDto.builder()
                .projectExternalId("")
                .technologyExternalId("")
                .build();
        return dto;
    }
}