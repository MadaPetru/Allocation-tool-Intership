package ro.fortech.allocation.employees.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ro.fortech.allocation.assignments.repository.AssignmentRepository;
import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.dto.EmployeeEmailDto;
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employees.repository.EmployeeRepository;
import ro.fortech.allocation.technology.dto.TechnologyDto;
import ro.fortech.allocation.technology.model.Technology;
import ro.fortech.allocation.technology.repository.TechnologyRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ro.fortech.allocation.EmployeeFactory.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TechnologyRepository technologyRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Test
    public void save_givenEmployee_expectTheEmployee() throws ParseException {
        Employee employee = createEmployee();
        EmployeeDto employeeDto = createEmployeeDto();

        Technology technology = extractTechnologyFromSet(employee);

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(technologyRepository.findByExternalId(anyString())).thenReturn(Optional.of(technology));

        EmployeeDto resultedValue = employeeService.save(employeeDto);
        employeeDto.setUid(resultedValue.getUid());

        verify(employeeRepository, times(1)).save(any(Employee.class));

        assertEquals(resultedValue, employeeDto);
    }

    private Technology extractTechnologyFromSet(Employee employee) {
        return employee.getTechnologies()
                .stream()
                .findFirst()
                .get();
    }

    @Test
    public void update_givenUidAndUpdatedValues_expectTheUpdatedEmployee() throws ParseException {
        Employee employee = createEmployee();
        EmployeeDto employeeDto = createEmployeeDto();

        Technology technology = extractTechnologyFromSet(employee);

        when(technologyRepository.findByExternalId(any())).thenReturn(Optional.of((technology)));
        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDto result = employeeService.update(employeeDto, employee.getUid());

        verify(employeeRepository).save(any(Employee.class));

        assertEquals(employeeDto, result);
    }

    @Test
    public void findByUid_givenUid_expectTheEmployee() throws ParseException {
        Employee employee = createEmployee();
        EmployeeDto employeeDto = createEmployeeDto();

        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(Optional.of(employee));

        EmployeeDto resultedValue = employeeService.findByUid(employee.getUid());

        verify(employeeRepository, times(1)).findEmployeeByUid(employee.getUid());

        assertEquals(employeeDto, resultedValue);
    }

    @Test
    public void findAll_givenEmployees_expectTheEmployees() throws ParseException {
        Employee employee = createEmployee();

        Pageable pageable = PageRequest.of(3, 3);
        Page<Employee> userPage = new PageImpl<>(Collections.singletonList(employee));

        when(employeeRepository.findAll(pageable)).thenReturn(userPage);

        Page<EmployeeDto> userPageResult = employeeService.findAll(pageable);
        assertEquals(1, userPageResult.getTotalElements());
    }

    @Test
    public void deleteByUid_givenUid_expectStatusOk() throws ParseException {
        Employee employee = createEmployee();

        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(Optional.of(employee));
        when(assignmentRepository.findAssignmentsByEmployee(employee)).thenReturn(new ArrayList<>());

        employeeService.deleteByUid(employee.getUid());

        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    public void fromEntityToDto_givenEntity_expectDto() throws ParseException {
        Employee employee = createEmployee();
        EmployeeDto employeeDto = createEmployeeDto();

        EmployeeDto resultedValue = employeeService.fromEntityToDto(employee);

        assertEquals(employeeDto, resultedValue);
    }

    @Test
    public void fromDtoToEntity_givenDto_expectEntity() throws ParseException {
        Employee employee = createEmployee();
        EmployeeDto employeeDto = createEmployeeDto();

        Technology technology = extractTechnologyFromSet(employee);

        when(technologyRepository.findByExternalId(anyString())).thenReturn(Optional.of(technology));

        Employee resultedValue = employeeService.fromDtoToEntity(employeeDto);

        assertEquals(employee.getId(), resultedValue.getId());
        assertEquals(employee.getUid(), resultedValue.getUid());
        assertEquals(employee.getEmail(), resultedValue.getEmail());
        assertEquals(employee.getTechnologies(), resultedValue.getTechnologies());
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void findByUid_givenUid_expectEmployeeNotFoundException() {
        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(Optional.empty());

        employeeService.findByUid("1");
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void update_givenUidAndUpdatedValues_expectEmployeeNotFoundException() throws ParseException {
        EmployeeDto employeeDto = createEmployeeDto();

        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(Optional.empty());

        employeeService.update(employeeDto, "1");
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void delete_givenUid_expectEmployeeNotFoundException() {
        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(Optional.empty());

        employeeService.deleteByUid("1");
    }

    @Test
    public void givenATechnologyToAddToAnEmployee_ShouldReturnAnEmployeeWithTheAddedTechnology() throws ParseException {
        Technology toBeAddedTechnology = Technology.builder().name("SomeTechnology").externalId("SomeExternalID").build();
        TechnologyDto toBeAddedTechnologyDto = TechnologyDto.builder().name("SomeTechnology").externalId("SomeExternalID").build();

        Employee employee = createEmployee();
        EmployeeDto employeeDto = createEmployeeDto();

        employeeDto.getTechnologies().add(toBeAddedTechnologyDto);

        when(technologyRepository.findByExternalId("SomeExternalID")).thenReturn(Optional.of(toBeAddedTechnology));
        when(employeeRepository.findEmployeeByUid(employee.getUid())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDto resultedValue = employeeService.addTechnologyToEmployee(employee.getUid(), toBeAddedTechnology.getExternalId());

        assertEquals(employeeDto, resultedValue);

        verify(technologyRepository, times(1)).findByExternalId("SomeExternalID");
        verify(employeeRepository, times(1)).findEmployeeByUid(employee.getUid());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    public void givenAnEntity_ShouldReturnTheCorrespondingEmailDTO() throws ParseException {
        EmployeeEmailDto employeeEmailDto = createEmployeeEmailDto();
        Employee employee = createEmployee();

        employee.setEmail(employeeEmailDto.getEmail());
        employee.setUid(employeeEmailDto.getUid());

        EmployeeEmailDto resultedValue = employeeService.fromEntityToResponseDto(employee);

        assertEquals(employeeEmailDto, resultedValue);
    }

    @Test
    public void givenAListOfEmployeeEntities_ShouldReturnTheCorrespondingEmailDTOs() throws ParseException {
        List<Employee> employees = generateEmployees(3);

        for (Employee e : employees) {
            e.setEmail("some_email@gmail.com");
        }

        when(employeeRepository.findEmployeeByEmail(anyString())).thenReturn(employees);

        List<EmployeeEmailDto> employeeEmailDtos = employeeService.findEmployeeByEmail("some_email@gmail.com");

        assertEquals(3, employeeEmailDtos.size());
    }


}