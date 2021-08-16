package ro.fortech.allocation.technology.handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ro.fortech.allocation.technology.dto.TechnologyDto;
import ro.fortech.allocation.technology.exception.TechnologyAlreadyExistsInTheDatabase;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ApiExceptionHandlerTest {
    private final TechnologyApiExceptionHandler exceptionHandler = new TechnologyApiExceptionHandler();

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Test
    public void handleConstraintViolationException_givenConstraintViolation_expectBadRequestStatus() {
        TechnologyDto nonValidDto = makeNonValidDto();
        Set<ConstraintViolation<TechnologyDto>> constraintViolationSet = validator.validateProperty(nonValidDto, "name");

        ConstraintViolationException constraintViolationException = new ConstraintViolationException(constraintViolationSet);

        ResponseEntity<Object> responseEntity = exceptionHandler.handleConstraintViolationException(constraintViolationException);
        assertEquals("name: name can't be null or empty", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void handleTechnologyNotFoundException_givenTechnology_expectBadRequestStatus() {
        TechnologyNotFoundByExternalIdException ex = new TechnologyNotFoundByExternalIdException("test");

        ResponseEntity<Object> responseEntity = exceptionHandler.handleTechnologyNotFoundByExternalIdException(ex);
        assertEquals("Could not find technology with id: test", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void handleTechnologyAlreadyExistException_givenTechnology_expectBadRequestStatus() {
        TechnologyAlreadyExistsInTheDatabase ex = new TechnologyAlreadyExistsInTheDatabase("test","test");

        ResponseEntity<Object> responseEntity = exceptionHandler.handleTechnologyAlreadyExist(ex);
        assertEquals("Technology with the name test already exists but is saved as test", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    public TechnologyDto makeNonValidDto(){
        return TechnologyDto.builder()
                .name("")
                .externalId("")
                .build();
    }
}