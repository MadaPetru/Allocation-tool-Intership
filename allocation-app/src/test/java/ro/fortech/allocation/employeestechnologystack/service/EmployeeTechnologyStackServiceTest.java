package ro.fortech.allocation.employeestechnologystack.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employees.repository.EmployeeRepository;
import ro.fortech.allocation.employeestechnologystack.dto.EmployeeTechnologyStackDto;
import ro.fortech.allocation.employeestechnologystack.exception.EmployeeTechnologyStackAlreadyExist;
import ro.fortech.allocation.employeestechnologystack.model.EmployeeTechnologyStack;
import ro.fortech.allocation.employeestechnologystack.model.EmployeeTechnologyStackKey;
import ro.fortech.allocation.employeestechnologystack.repository.EmployeeTechnologyStackRepository;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;
import ro.fortech.allocation.technology.model.Technology;
import ro.fortech.allocation.technology.repository.TechnologyRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeTechnologyStackServiceTest {
    @InjectMocks
    private EmployeeTechnologyStackService service;

    @Mock
    private EmployeeTechnologyStackRepository repository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TechnologyRepository technologyRepository;

    @Mock
    private ModelMapper mapper;

    @Test
    public void addEmployeeTechnologyStack_givenEmployeeAndTechnology_expectSaved() throws ParseException {
        EmployeeTechnologyStack employeeTechnologyStack = makeEntity();
        EmployeeTechnologyStackDto dto = makeDto();
        Employee employee = employeeTechnologyStack.getEmployee();
        Technology technology = employeeTechnologyStack.getTechnology();
        when(employeeRepository.findEmployeeByUid(any(String.class))).thenReturn(Optional.of(employee));
        when(technologyRepository.findByExternalId(any(String.class))).thenReturn(Optional.of(technology));
        when(repository.findByTechnologyAndEmployee(any(Technology.class), any(Employee.class)))
                .thenReturn(Optional.empty());
        service.addEmployeeTechnologyStack(dto);
        verify(repository).save(any(EmployeeTechnologyStack.class));
    }

    @Test
    public void deleteEmployeeTechnologyStack_givenEmployeeAndTechnology_expectDeleted() throws ParseException {
        EmployeeTechnologyStack employeeTechnologyStack = makeEntity();
        EmployeeTechnologyStackDto dto = makeDto();
        Employee employee = employeeTechnologyStack.getEmployee();
        Technology technology = employeeTechnologyStack.getTechnology();
        when(employeeRepository.findEmployeeByUid(any(String.class))).thenReturn(Optional.of(employee));
        when(technologyRepository.findByExternalId(any(String.class))).thenReturn(Optional.of(technology));
        when(repository.findByTechnologyAndEmployee(any(Technology.class), any(Employee.class))).thenReturn(Optional.of(employeeTechnologyStack));
        service.deleteEmployeeTechnologyStack(dto);
        verify(repository).delete(employeeTechnologyStack);
    }

    @Test
    public void getAllTechnologiesByEmployeeId_givenEmployeeExternalId_expectedStatusOk() throws ParseException {
        EmployeeTechnologyStack employeeTechnologyStack = makeEntity();
        EmployeeTechnologyStackDto dto = makeDto();
        Employee employee = employeeTechnologyStack.getEmployee();
        Technology technology = employeeTechnologyStack.getTechnology();
        when(employeeRepository.findEmployeeByUid(any(String.class))).thenReturn(Optional.of(employee));
        String employeeExternalId = employee.getUid();
        service.getAllTechnologiesByEmployeeId(employeeExternalId);

    }

    @Test(expected = EmployeeNotFoundException.class)
    public void addEmployeeTechnologyStack_givenEmployeeAndTechnology_expectEmployeeNotFound() throws ParseException {
        EmployeeTechnologyStack employeeTechnologyStack = makeEntity();
        EmployeeTechnologyStackDto dto = makeDto();
        Employee employee = employeeTechnologyStack.getEmployee();
        Technology technology = employeeTechnologyStack.getTechnology();
        String employeeExternalId = employee.getUid();
        when(employeeRepository.findEmployeeByUid(any(String.class)))
                .thenThrow(new EmployeeNotFoundException(employeeExternalId));
        service.addEmployeeTechnologyStack(dto);
    }

    @Test(expected = TechnologyNotFoundByExternalIdException.class)
    public void addEmployeeTechnologyStack_givenEmployeeAndTechnology_expectTechnologyNotFound() throws ParseException {
        EmployeeTechnologyStack employeeTechnologyStack = makeEntity();
        EmployeeTechnologyStackDto dto = makeDto();
        Employee employee = employeeTechnologyStack.getEmployee();
        Technology technology = employeeTechnologyStack.getTechnology();
        String technologyExternalId = technology.getExternalId();
        when(employeeRepository.findEmployeeByUid(any(String.class)))
                .thenReturn(Optional.ofNullable(employee));
        when(technologyRepository.findByExternalId(any(String.class)))
                .thenThrow(new TechnologyNotFoundByExternalIdException(technologyExternalId));
        service.addEmployeeTechnologyStack(dto);
    }

    @Test(expected = EmployeeTechnologyStackAlreadyExist.class)
    public void addEmployeeTechnologyStack_givenEmployeeAndTechnology_expectAlreadyExist() throws ParseException {
        EmployeeTechnologyStack employeeTechnologyStack = makeEntity();
        EmployeeTechnologyStackDto dto = makeDto();
        Employee employee = employeeTechnologyStack.getEmployee();
        Technology technology = employeeTechnologyStack.getTechnology();
        when(employeeRepository.findEmployeeByUid(any(String.class))).thenReturn(Optional.ofNullable(employee));
        when(technologyRepository.findByExternalId(any(String.class))).thenReturn(Optional.ofNullable(technology));
        when(repository.findByTechnologyAndEmployee(any(Technology.class), any(Employee.class)).isPresent())
                .thenThrow(new EmployeeTechnologyStackAlreadyExist());
        service.addEmployeeTechnologyStack(dto);
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void deleteEmployeeTechnologyStack_givenEmployeeAndTechnology_expectEmployeeNotFound() throws ParseException {
        EmployeeTechnologyStack employeeTechnologyStack = makeEntity();
        EmployeeTechnologyStackDto dto = makeDto();
        Employee employee = employeeTechnologyStack.getEmployee();
        Technology technology = employeeTechnologyStack.getTechnology();
        String employeeExternalId = employee.getUid();
        when(employeeRepository.findEmployeeByUid(any(String.class)))
                .thenThrow(new EmployeeNotFoundException(employeeExternalId));
        service.deleteEmployeeTechnologyStack(dto);
    }

    @Test(expected = TechnologyNotFoundByExternalIdException.class)
    public void deleteEmployeeTechnologyStack_givenEmployeeAndTechnology_expectTechnologyNotFound() throws ParseException {
        EmployeeTechnologyStack employeeTechnologyStack = makeEntity();
        EmployeeTechnologyStackDto dto = makeDto();
        Employee employee = employeeTechnologyStack.getEmployee();
        Technology technology = employeeTechnologyStack.getTechnology();
        String technologyExternalId = technology.getExternalId();
        when(employeeRepository.findEmployeeByUid(any(String.class)))
                .thenReturn(Optional.ofNullable(employee));
        when(technologyRepository.findByExternalId(any(String.class)))
                .thenThrow(new TechnologyNotFoundByExternalIdException(technologyExternalId));
        service.deleteEmployeeTechnologyStack(dto);
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void getAllTechnologiesByEmployeeId_givenEmployeeExternalId_expectedEmployeeNotFound() throws ParseException {
        EmployeeTechnologyStack employeeTechnologyStack = makeEntity();
        EmployeeTechnologyStackDto dto = makeDto();
        Employee employee = employeeTechnologyStack.getEmployee();
        Technology technology = employeeTechnologyStack.getTechnology();
        String employeeExternalId = employee.getUid();
        when(employeeRepository.findEmployeeByUid(any(String.class)))
                .thenThrow(new EmployeeNotFoundException(employeeExternalId));
        service.getAllTechnologiesByEmployeeId(employeeExternalId);

    }


    private EmployeeTechnologyStack makeEntity() throws ParseException {
        Set<EmployeeTechnologyStack> set = new HashSet<>();
        Employee employee = Employee.builder()
                .id(1L)
                .uid("22")
                .email("adsa@yahoo.com")
                .name("aaa")
                .unit("3")
                .businessUnit("businessUnit")
                .supervisor("supervisor")
                .workingHours(4)
                .active(true)
                .internalPosition("aaaa")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .technicalExpertise("technicalExpertise")
                .build();
        Technology technology = Technology.builder()
                .name("tech")
                .id(1L)
                .externalId("externalId")
                .build();
        EmployeeTechnologyStack employeeTechnologyStack = new EmployeeTechnologyStack();
        employeeTechnologyStack.setEmployee(employee);
        employeeTechnologyStack.setTechnology(technology);
        Long employeeId = employee.getId();
        Long technologyId = technology.getId();
        EmployeeTechnologyStackKey key = new EmployeeTechnologyStackKey(employeeId, technologyId);
        employeeTechnologyStack.setId(key);
        return employeeTechnologyStack;
    }

    private EmployeeTechnologyStackDto makeDto() throws ParseException {
        EmployeeTechnologyStack employeeTechnologyStack = makeEntity();
        Technology technology = employeeTechnologyStack.getTechnology();
        Employee employee = employeeTechnologyStack.getEmployee();
        String technologyExternalId = technology.getExternalId();
        String employeeExternalId = employee.getUid();
        EmployeeTechnologyStackDto dto = new EmployeeTechnologyStackDto();
        dto.setTechnologyExternalId(technologyExternalId);
        dto.setEmployeeUid(employeeExternalId);
        return dto;
    }
}