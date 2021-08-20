package ro.fortech.allocation.employees.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.dto.EmployeeEmailDto;
import ro.fortech.allocation.employees.service.EmployeeService;
import ro.fortech.allocation.technology.dto.TechnologyDto;

import javax.validation.ConstraintViolation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ro.fortech.allocation.EmployeeFactory.createEmployeeDto;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@EnableSpringDataWebSupport
public class EmployeeControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Before
    public void setup() {
        TimeZone defaultTimeZone = TimeZone.getTimeZone("UTC");
        TimeZone.setDefault(defaultTimeZone);
    }

    @Test
    public void findAll_givenEmployees_expectTheEmployees() throws Exception {
        EmployeeDto employeeDto = makeDto();
        Page<EmployeeDto> page = new PageImpl<>(Collections.singletonList(employeeDto));
        Pageable pageable = PageRequest.of(3, 3);

        when(employeeService.findAll(pageable)).thenReturn(page);
        mockMvc.perform(get("/employees").content(mapper.writeValueAsString(pageable)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(employeeService).findAll(Mockito.any(Pageable.class));

    }

    @Test
    public void findEmployeesByEmail_givenEmail_expectTheEmployees() throws Exception {
        EmployeeEmailDto employeeEmailDto = EmployeeEmailDto.builder()
                .uid("22")
                .email("adsa@yahoo.com")
                .build();

        List<EmployeeEmailDto> employeeEmailDtoList = new ArrayList<>();
        employeeEmailDtoList.add(employeeEmailDto);

        Mockito.when(
                employeeService.findEmployeeByEmail("adsa")).thenReturn(employeeEmailDtoList);

        mockMvc.perform(get("/employees/search").param("email", "adsa"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("adsa@yahoo.com"));

        verify(employeeService, times(1)).findEmployeeByEmail("adsa");

    }

    @Test
    public void validation_givenNonValid_expectConstraintViolation() {
        EmployeeDto nonValidEmployeeDto = makeNonValidDto();
        Set<ConstraintViolation<EmployeeDto>> constraintViolationSet = validator.validateProperty(nonValidEmployeeDto, "name");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertEquals("must not be empty", constraintViolationSet.iterator().next().getMessage());

        constraintViolationSet = validator.validateProperty(nonValidEmployeeDto, "email");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertTrue(constraintViolationSet.iterator().next().getMessage().contains("must match"));

        constraintViolationSet = validator.validateProperty(nonValidEmployeeDto, "workingHours");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertEquals("must be greater than or equal to 1", constraintViolationSet.iterator().next().getMessage());

        constraintViolationSet = validator.validateProperty(nonValidEmployeeDto, "internalPosition");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertEquals("must not be empty", constraintViolationSet.iterator().next().getMessage());

        constraintViolationSet = validator.validateProperty(nonValidEmployeeDto, "active");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertEquals("must not be null", constraintViolationSet.iterator().next().getMessage());

        constraintViolationSet = validator.validateProperty(nonValidEmployeeDto, "startDate");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertEquals("must not be null", constraintViolationSet.iterator().next().getMessage());

        constraintViolationSet = validator.validateProperty(nonValidEmployeeDto, "endDate");
        Assertions.assertEquals(1, constraintViolationSet.size());
        Assertions.assertEquals("must not be null", constraintViolationSet.iterator().next().getMessage());

    }

    @Test
    public void findEmployeeByUid_givenUid_expectTheEmployee() throws Exception {
        EmployeeDto request = makeDto();
        EmployeeDto response = makeDto();

        when(employeeService.findByUid(Mockito.anyString())).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(get("/employees/" + request.getUid())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        EmployeeDto result = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<EmployeeDto>() {
        });
        assertEquals(request, result);
        verify(employeeService).findByUid(request.getUid());
    }

    @Test
    public void addEmployee_givenEmployee_expectTheCreatedEmployee() throws Exception {
        EmployeeDto request = makeDto();
        when(employeeService.save(Mockito.any(EmployeeDto.class))).thenReturn(request);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.internalPosition").value(request.getInternalPosition()));
        verify(employeeService).save(request);
    }

    @Test
    public void deleteEmployee_givenUid_expectStatusOk() throws Exception {
        EmployeeDto employeeDto = makeDto();
        mockMvc.perform(delete("/employees/" + employeeDto.getUid())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(employeeService).deleteByUid(employeeDto.getUid());
    }

    @Test
    public void updateEmployee_givenUidAndUpdatedEmployee_expectUpdatedEmployee() throws Exception {
        EmployeeDto employeeDto = makeDto();
        when(employeeService.update(Mockito.any(EmployeeDto.class), Mockito.anyString())).thenReturn(employeeDto);
        mockMvc.perform(put("/employees/" + employeeDto.getUid())
                        .content(mapper.writeValueAsString(employeeDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(employeeDto.getName()))
                .andExpect(jsonPath("$.email").value(employeeDto.getEmail()))
                .andExpect(jsonPath("$.internalPosition").value(employeeDto.getInternalPosition()));

        verify(employeeService).update(employeeDto, employeeDto.getUid());
    }

    @Test
    public void addTechnologyToEmployee_expectEmployeeWithAddedTechnology() throws Exception {
        EmployeeDto employeeDto = createEmployeeDto();
        TechnologyDto someTech = TechnologyDto.builder().name("SomeTech").externalId("SomeExternalUID").build();

        employeeDto.getTechnologies().add(someTech);

        Iterator iterator = employeeDto.getTechnologies().iterator();

        when(employeeService.addTechnologyToEmployee(employeeDto.getUid(), someTech.getExternalId())).thenReturn(employeeDto);

        mockMvc.perform(patch("/employees/" + employeeDto.getUid())
                        .param("externalId", "SomeExternalUID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.technologies[0]").value(iterator.next()))
                .andExpect(jsonPath("$.technologies[1]").value(iterator.next()));
    }

    private EmployeeDto makeDto() throws ParseException {
        return EmployeeDto.builder()
                .active(true)
                .email("andrei@yahoo.com")
                .name("Andrei")
                .internalPosition("Junior")
                .uid("22")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();
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