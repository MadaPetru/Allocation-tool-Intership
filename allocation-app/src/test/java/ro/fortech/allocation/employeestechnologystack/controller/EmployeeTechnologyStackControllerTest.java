package ro.fortech.allocation.employeestechnologystack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employeestechnologystack.dto.EmployeeTechnologyStackDto;
import ro.fortech.allocation.employeestechnologystack.exception.EmployeeTechnologyNotFound;
import ro.fortech.allocation.employeestechnologystack.model.EmployeeTechnologyStack;
import ro.fortech.allocation.employeestechnologystack.model.EmployeeTechnologyStackKey;
import ro.fortech.allocation.employeestechnologystack.service.EmployeeTechnologyStackService;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;
import ro.fortech.allocation.technology.model.Technology;

import javax.validation.ConstraintViolation;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@EnableWebMvc
public class EmployeeTechnologyStackControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeTechnologyStackService stackService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private LocalValidatorFactoryBean validator;

    @Test
    public void validation_givenNonValid_expectConstraintViolation() throws ParseException {
        EmployeeTechnologyStackDto dto = mapper.map(makeEntity(), EmployeeTechnologyStackDto.class);
        EmployeeTechnologyStackDto nonValidDto = EmployeeTechnologyStackDto.builder()
                .employeeUid("")
                .technologyExternalId("")
                .build();
        Set<ConstraintViolation<EmployeeTechnologyStackDto>> constraintViolationSet =
                validator.validateProperty(nonValidDto, "employeeUid");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertEquals("name can not be empty", constraintViolationSet.iterator().next().getMessage());

        Assertions.assertEquals(1, constraintViolationSet.size());
        constraintViolationSet = validator.validateProperty(nonValidDto, "technologyExternalId");
        Assertions.assertEquals("name can not be empty", constraintViolationSet.iterator().next().getMessage());
    }

    @Test
    public void addEmployeeTechnology_givenEmployeeAndTechnology_expectEmployeeTechnology() throws Exception {
        EmployeeTechnologyStack employeeTechnologyStack = makeEntity();
        EmployeeTechnologyStackDto dto = mapper.map(employeeTechnologyStack, EmployeeTechnologyStackDto.class);
        when(stackService.addEmployeeTechnologyStack(any())).thenReturn(dto);
        mockMvc.perform(post("/employeeTechnology")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(stackService).addEmployeeTechnologyStack(dto);
    }

    @Test
    public void addEmployeeTechnology_givenEmployeeAndTechnology_expectEmployeeNotFound() throws Exception {
        EmployeeTechnologyStack employeeTechnologyStack = makeEntity();
        EmployeeTechnologyStackDto dto = mapper.map(employeeTechnologyStack, EmployeeTechnologyStackDto.class);
        when(stackService.addEmployeeTechnologyStack(any(EmployeeTechnologyStackDto.class)))
                .thenThrow(new EmployeeTechnologyNotFound());
        mockMvc.perform(post("/employeeTechnology")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(stackService, times(1)).addEmployeeTechnologyStack(dto);
    }

    @Test
    public void addEmployeeTechnology_givenEmployeeAndTechnology_expectTechnologyNotFound() throws Exception {
        EmployeeTechnologyStack employeeTechnologyStack = makeEntity();
        EmployeeTechnologyStackDto dto = mapper.map(employeeTechnologyStack, EmployeeTechnologyStackDto.class);
        String technologyExternalId = employeeTechnologyStack.getTechnology().getExternalId();
        when(stackService.addEmployeeTechnologyStack(any(EmployeeTechnologyStackDto.class)))
                .thenThrow(new TechnologyNotFoundByExternalIdException(technologyExternalId));
        mockMvc.perform(post("/employeeTechnology")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(stackService, times(1)).addEmployeeTechnologyStack(dto);
    }

    @Test
    public void deleteEmployeeTechnology_givenEmployeeTechnologyStackDto_expectStatusIsOk() throws Exception {
        EmployeeTechnologyStack employeeTechnologyStack = makeEntity();
        EmployeeTechnologyStackDto dto = mapper.map(employeeTechnologyStack, EmployeeTechnologyStackDto.class);
        mockMvc.perform(delete("/employeeTechnology")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(stackService, times(1)).deleteEmployeeTechnologyStack(dto);
    }

    @Test
    public void getTechnologiesOfAnEmployee_givenEmployeeExternalId_expectStatusIsOkAndTechnologies() throws Exception {
        EmployeeTechnologyStack employeeTechnologyStack = makeEntity();
        String employeeExternalId = employeeTechnologyStack.getEmployee().getUid();
        List<ro.fortech.allocation.technology.dto.TechnologyDto> technologies = new ArrayList<>();
        technologies.add(new ro.fortech.allocation.technology.dto.TechnologyDto("DtoOne", "SomeId"));
        technologies.add(new ro.fortech.allocation.technology.dto.TechnologyDto("DtoTwo", "SomeOtherId"));
        technologies.add(new ro.fortech.allocation.technology.dto.TechnologyDto("DtoThree", "ThirdId"));
        when(stackService.getAllTechnologiesByEmployeeId(any())).thenReturn(technologies);
        mockMvc.perform(get("/employeeTechnology/" + employeeExternalId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(technologies)));
        verify(stackService, times(1)).getAllTechnologiesByEmployeeId(employeeExternalId);
    }

    private EmployeeTechnologyStack makeEntity() {
        Set<EmployeeTechnologyStack> set = new HashSet<>();
        Employee employee = Employee.builder()
                .id(5L)
                .uid("someUIDForTest")
                .build();
        Technology technology = Technology.builder()
                .id(1L)
                .name("name")
                .externalId("externalId")
                .build();
        EmployeeTechnologyStack employeeTechnologyStack = EmployeeTechnologyStack.builder()
                .employee(employee)
                .technology(technology)
                .id(new EmployeeTechnologyStackKey(employee.getId(), technology.getId()))
                .build();
        return employeeTechnologyStack;
    }
}