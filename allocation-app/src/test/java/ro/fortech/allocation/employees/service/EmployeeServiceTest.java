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
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employees.repository.EmployeeRepository;
import ro.fortech.allocation.technology.model.Technology;
import ro.fortech.allocation.technology.repository.TechnologyRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        Technology techOne = Technology.builder().name("TechOne").build();
        Set<Technology> technologySet = new HashSet<>(Arrays.asList(techOne));
        Set<String> technologyNames = new HashSet<>(Arrays.asList("TechOne"));

        EmployeeDto employeeDto = EmployeeDto.builder()
                .email("EmployeeOne@yahoo.com")
                .name("EmployeeOne")
                .technologies(technologyNames)
                .active(true)
                .internalPosition("Employee")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();

        Employee employee = Employee.builder()
                .email("EmployeeOne@yahoo.com")
                .name("EmployeeOne")
                .technologies(technologySet)
                .active(true)
                .internalPosition("Employee")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();

        when(technologyRepository.findByName(any())).thenReturn(Optional.of(techOne));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDto result = employeeService.save(employeeDto);
        employeeDto.setUid(result.getUid());
        verify(employeeRepository).save(any(Employee.class));

        assertEquals(employeeDto, result);
    }

    @Test
    public void update_givenUidAndUpdatedValues_expectTheUpdatedEmployee() throws ParseException {
        Technology techOne = Technology.builder().name("TechOne").build();
        Set<Technology> technologySet = new HashSet<>(Arrays.asList(techOne));
        Set<String> technologyNames = new HashSet<>(Arrays.asList("TechOne"));

        EmployeeDto employeeDto = EmployeeDto.builder()
                .uid("22")
                .email("EmployeeDto@yahoo.com")
                .name("EmployeeDtoOne")
                .technologies(technologyNames)
                .active(true)
                .internalPosition("Employee")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();

        Employee employee = Employee.builder()
                .uid("22")
                .email(employeeDto.getEmail())
                .name(employeeDto.getName())
                .technologies(technologySet)
                .active(employeeDto.getActive())
                .internalPosition(employeeDto.getInternalPosition())
                .startDate(employeeDto.getStartDate())
                .endDate(employeeDto.getEndDate())
                .build();

        when(technologyRepository.findByName(any())).thenReturn(Optional.of(techOne));
        when(employeeRepository.findEmployeeByUid("22")).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDto result = employeeService.update(employeeDto, "22");

        verify(employeeRepository).save(any(Employee.class));

        assertEquals(employeeDto, result);
    }

    @Test
    public void findByUid_givenUid_expectTheEmployee() throws ParseException {
        Technology techOne = Technology.builder().name("TechOne").build();
        Set<Technology> technologySet = new HashSet<>(Arrays.asList(techOne));

        Employee employee = Employee.builder()
                .uid("22")
                .email("EmployeeOne@yahoo.com")
                .name("EmployeeOne")
                .technologies(technologySet)
                .active(true)
                .internalPosition("Employee")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();

        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(Optional.of(employee));

        EmployeeDto fetchedEmployee = employeeService.findByUid(employee.getUid());

        verify(employeeRepository).findEmployeeByUid("22");

        assertEquals(fetchedEmployee.getName(), employee.getName());
        assertEquals(fetchedEmployee.getEmail(), employee.getEmail());
        assertEquals(fetchedEmployee.getActive(), employee.getActive());
        assertEquals(fetchedEmployee.getInternalPosition(), employee.getInternalPosition());
        assertEquals(fetchedEmployee.getStartDate(), employee.getStartDate());
        assertEquals(fetchedEmployee.getEndDate(), employee.getEndDate());

    }

    @Test
    public void findAll_givenEmployees_expectTheEmployees() throws ParseException {
        Technology techOne = Technology.builder().name("TechOne").build();
        Set<Technology> technologySet = new HashSet<>(Arrays.asList(techOne));

        Employee randomEmployee = Employee.builder()
                .uid("22")
                .email("RandomEmployee@yahoo.com")
                .name("RandomEmployee")
                .technologies(technologySet)
                .active(true)
                .internalPosition("Employee")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();

        Pageable pageable = PageRequest.of(3, 3);
        Page<Employee> userPage = new PageImpl<>(Collections.singletonList(randomEmployee));

        when(employeeRepository.findAll(pageable)).thenReturn(userPage);

        Page<EmployeeDto> userPageResult = employeeService.findAll(pageable);
        assertEquals(1, userPageResult.getTotalElements());
    }

    @Test
    public void deleteByUid_givenUid_expectStatusOk() throws ParseException {
        Employee employee = Employee.builder()
                .uid("22")
                .email("adsa@yahoo.com")
                .name("aaa")
                .active(true)
                .internalPosition("aaaa")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();
        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(Optional.of(employee));
        when(assignmentRepository.findAssignmentsByEmployee(employee)).thenReturn(new ArrayList<>());

        employeeService.deleteByUid(employee.getUid());
        verify(employeeRepository).delete(employee);
    }

    @Test
    public void fromEntityToDto_givenEntity_expectDto() throws ParseException {
        Technology techOne = Technology.builder().name("TechOne").build();
        Set<Technology> technologySet = new HashSet<>(Arrays.asList(techOne));

        Employee employee = Employee.builder()
                .uid("22")
                .email("adsa@yahoo.com")
                .name("aaa")
                .active(true)
                .internalPosition("aaaa")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .technologies(technologySet)
                .build();

        EmployeeDto employeeDto = employeeService.fromEntityToDto(employee);

        assertEquals(employeeDto.getName(), employee.getName());
        assertEquals(employeeDto.getEmail(), employee.getEmail());
        assertEquals(employeeDto.getActive(), employee.getActive());
        assertEquals(employeeDto.getInternalPosition(), employee.getInternalPosition());
        assertEquals(employeeDto.getStartDate(), employee.getStartDate());
        assertEquals(employeeDto.getEndDate(), employee.getEndDate());

    }

    @Test
    public void fromDtoToEntity_givenDto_expectEntity() throws ParseException {
        Technology technology = Technology.builder().name("TechOne").build();
        Set<String> technologySet = new HashSet<>(Arrays.asList("TechOne"));

        EmployeeDto employeeDto = EmployeeDto.builder()
                .uid("22")
                .email("adsa@yahoo.com")
                .name("aaa")
                .active(true)
                .internalPosition("aaaa")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .technologies(technologySet)
                .build();

        when(technologyRepository.findByName(any()))
                .thenReturn(Optional.ofNullable(Technology.builder()
                        .name("TechOne").build()));

        Employee employee = employeeService.fromDtoToEntity(employeeDto);

        assertEquals(employeeDto.getName(), employee.getName());
        assertEquals(employeeDto.getEmail(), employee.getEmail());
        assertEquals(employeeDto.getActive(), employee.getActive());
        assertEquals(employeeDto.getInternalPosition(), employee.getInternalPosition());
        assertEquals(employeeDto.getStartDate(), employee.getStartDate());
        assertEquals(employeeDto.getEndDate(), employee.getEndDate());
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void findByUid_givenUid_expectEmployeeNotFoundException() {
        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(Optional.empty());
        employeeService.findByUid("1");
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void update_givenUidAndUpdatedValues_expectEmployeeNotFoundException() throws ParseException {
        EmployeeDto employeeDto = EmployeeDto.builder()
                .uid("22")
                .email("adsa@yahoo.com")
                .name("aaa")
                .active(true)
                .internalPosition("aaaa")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();
        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(Optional.empty());
        employeeService.update(employeeDto, "1");
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void delete_givenUid_expectEmployeeNotFoundException() {
        when(employeeRepository.findEmployeeByUid(anyString())).thenReturn(Optional.empty());
        employeeService.deleteByUid("1");
    }
}