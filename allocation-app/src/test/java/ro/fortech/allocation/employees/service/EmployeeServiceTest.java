package ro.fortech.allocation.employees.service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.dto.EmployeeEmailDto;
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employees.repository.EmployeeRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ModelMapper modelMapper;
    @Test
    public void save_givenEmployee_expectTheEmployee() throws ParseException {
        EmployeeDto employeeDto = EmployeeDto.builder()
                .email("adsa@yahoo.com")
                .name("aaa")
                .active(true)
                .internalPosition("aaaa")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();
        Employee employee = Employee.builder()
                .email("adsa@yahoo.com")
                .name("aaa")
                .active(true)
                .internalPosition("aaaa")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();

        when(modelMapper.map(employeeDto, Employee.class)).thenReturn(employee);
        when(modelMapper.map(employee, EmployeeDto.class)).thenReturn(employeeDto);
        when(employeeRepository.save(employee)).thenReturn(employee);
        EmployeeDto result = employeeService.save(employeeDto);
        employeeDto.setUid(result.getUid());
        assertEquals(employeeDto.getName(), result.getName());
        assertEquals(employeeDto.getEmail(), result.getEmail());
        assertEquals(employeeDto.getWorkingHours(), result.getWorkingHours());
    }

    @Test
    public void getEmployees_givenEmail_expectTheEmployees() throws ParseException {
        Employee employee = Employee.builder()
                .uid("22")
                .email("adsa@yahoo.com")
                .name("aaa")
                .active(true)
                .internalPosition("aaaa")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();

        Employee employee2 = Employee.builder()
                .uid("22")
                .email("adsa@yahoo.com")
                .name("aaa")
                .active(true)
                .internalPosition("aaaa")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();

        EmployeeEmailDto employeeEmailDto = EmployeeEmailDto.builder()
                .uid("22")
                .email("adsa@yahoo.com")
                .build();

        EmployeeEmailDto employeeEmailDto2 = EmployeeEmailDto.builder()
                .uid("22")
                .email("adsa@yahoo.com")
                .build();

        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        employees.add(employee2);

        List<EmployeeEmailDto> employeeEmailDtoList = new ArrayList<>();
        employeeEmailDtoList.add(employeeEmailDto);
        employeeEmailDtoList.add(employeeEmailDto2);

        when(employeeRepository.findEmployeeByEmail("adsa")).thenReturn(employees);

        List<EmployeeEmailDto> employeeEmailDtoList2 = employeeService.findEmployeeByEmail("adsa");

        assertEquals(employeeEmailDtoList2.size(), employeeEmailDtoList.size());
        assertEquals(employeeEmailDtoList2.get(0).getEmail(), employeeEmailDtoList.get(0).getEmail());
    }


    @Test
    public void update_givenUidAndUpdatedValues_expectTheUpdatedEmployee() throws ParseException {
        EmployeeDto employeeDto = EmployeeDto.builder()
                .email("adsa@yahoo.com")
                .name("aaa")
                .active(true)
                .internalPosition("aaaa")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();
        Employee employee = Employee.builder()
                .uid("22")
                .email("adsa@yahoo.com")
                .name("aaa")
                .active(true)
                .internalPosition("aaaa")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();
        employeeDto.setUid(employee.getUid());
        when(employeeRepository.findEmployeeByUid(employee.getUid())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(modelMapper.map(employee, EmployeeDto.class)).thenReturn(employeeDto);
        when(modelMapper.map(employeeDto, Employee.class)).thenReturn(employee);
        EmployeeDto result = employeeService.update(employeeDto, employee.getUid());
        verify(employeeRepository).save(any(Employee.class));
        assertEquals(employeeDto.getName(), result.getName());
    }
    @Test
    public void findByUid_givenUid_expectTheEmployee() throws ParseException {
        EmployeeDto employeeDto = EmployeeDto.builder()
                .email("adsa@yahoo.com")
                .name("aaa")
                .active(true)
                .internalPosition("aaaa")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();
        Employee employee = Employee.builder()
                .uid("22")
                .email("adsa@yahoo.com")
                .name("aaa")
                .active(true)
                .internalPosition("aaaa")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();
        employeeDto.setUid(employee.getUid());
        when(employeeRepository.findEmployeeByUid(employee.getUid())).thenReturn(Optional.of(employee));
        when(modelMapper.map(employee, EmployeeDto.class)).thenReturn(employeeDto);
        EmployeeDto fetchedEmployee = employeeService.findByUid(employee.getUid());
        verify(employeeRepository).findEmployeeByUid(employee.getUid());
        assertEquals(fetchedEmployee.getName(), employee.getName());
        assertEquals(fetchedEmployee.getEmail(), employee.getEmail());
        assertEquals(fetchedEmployee.getActive(), employee.getActive());
        assertEquals(fetchedEmployee.getInternalPosition(), employee.getInternalPosition());
        assertEquals(fetchedEmployee.getStartDate(), employee.getStartDate());
        assertEquals(fetchedEmployee.getEndDate(), employee.getEndDate());
    }
    @Test
    public void findAll_givenEmployees_expectTheEmployees() throws ParseException {
        Employee employee1 = Employee.builder()
                .uid("22")
                .email("adsa@yahoo.com")
                .name("aaa")
                .active(true)
                .internalPosition("aaaa")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();
        Pageable pageable = PageRequest.of(3, 3);
        Page<Employee> userPage = new PageImpl<>(Collections.singletonList(employee1));
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
        employeeService.deleteByUid(employee.getUid());
        verify(employeeRepository).delete(employee);
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