package ro.fortech.allocation.employees.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ro.fortech.allocation.assignments.repository.AssignmentRepository;
import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.dto.EmployeeEmailDto;
import ro.fortech.allocation.employees.exception.CsvParseException;
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;
import ro.fortech.allocation.employees.helper.CsvHelper;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employees.repository.EmployeeRepository;
import ro.fortech.allocation.technology.dto.TechnologyDto;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;
import ro.fortech.allocation.technology.model.Technology;
import ro.fortech.allocation.technology.repository.TechnologyRepository;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final TechnologyRepository technologyRepository;
    private final AssignmentRepository assignmentRepository;

    public EmployeeDto save(@Valid EmployeeDto employeeDto) {
        employeeDto.setUid(UUID.randomUUID().toString());
        return fromEntityToDto(employeeRepository.save(fromDtoToEntity(employeeDto)));
    }

    public void save(MultipartFile file) throws IOException {
        if (!CsvHelper.hasCSVFormat(file))
            throw new CsvParseException("");
        employeeRepository.saveAll(CsvHelper.csvToEmployees(file.getInputStream()));
    }

    public EmployeeDto update(@Valid EmployeeDto employeeDto, String Uid) {
        Employee oldEmployee = employeeRepository.findEmployeeByUid(Uid).orElseThrow(() -> new EmployeeNotFoundException(Uid));
        Employee updatedEmployee = fromDtoToEntity(employeeDto);
        updatedEmployee.setId(oldEmployee.getId());
        updatedEmployee.setUid(oldEmployee.getUid());
        return fromEntityToDto(employeeRepository.save(updatedEmployee));
    }

    public EmployeeDto findByUid(String uid) {
        return fromEntityToDto(employeeRepository.findEmployeeByUid(uid).orElseThrow(() -> new EmployeeNotFoundException(uid)));
    }

    public Page<EmployeeDto> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(this::fromEntityToDto);
    }

    public void deleteByUid(String uid) {

        Employee employee = employeeRepository.findEmployeeByUid(uid).orElseThrow(() -> new EmployeeNotFoundException(uid));
        assignmentRepository.deleteAll(assignmentRepository.findAssignmentsByEmployee(employee));

        employeeRepository.delete(employee);
    }

    public List<EmployeeEmailDto> findEmployeeByEmail(String email) {
        List<Employee> employees = employeeRepository.findEmployeeByEmail(email);
        List<EmployeeEmailDto> employeeResponseDtoList = new ArrayList<>();
        for (Employee e : employees) {
            employeeResponseDtoList.add(fromEntityToResponseDto(e));
        }
        return employeeResponseDtoList;
    }

    public EmployeeEmailDto fromEntityToResponseDto(Employee employee) {
        return EmployeeEmailDto.builder()
                .email(employee.getEmail())
                .uid(employee.getUid())
                .build();
    }

    public EmployeeDto fromEntityToDto(Employee employee) {
        Set<Technology> technologies = employee.getTechnologies();

        Set<TechnologyDto> technologyDtos = mapTechnologiesToDtos(technologies);

        return EmployeeDto.builder()
                .uid(employee.getUid())
                .email(employee.getEmail())
                .active(employee.getActive())
                .endDate(employee.getEndDate())
                .internalPosition(employee.getInternalPosition())
                .name(employee.getName())
                .startDate(employee.getStartDate())
                .supervisor(employee.getSupervisor())
                .unit(employee.getUnit())
                .workingHours(employee.getWorkingHours())
                .technologies(technologyDtos)
                .build();
    }

    public Employee fromDtoToEntity(EmployeeDto employeeDto) {

        Set<TechnologyDto> technologies = employeeDto.getTechnologies();

        Set<Technology> actualTechnologies = technologies.stream().map(t -> searchForTechnology(t.getExternalId())).collect(Collectors.toSet());

        return Employee.builder()
                .uid(employeeDto.getUid())
                .active(employeeDto.getActive())
                .businessUnit(employeeDto.getBusinessUnit())
                .email(employeeDto.getEmail())
                .endDate(employeeDto.getEndDate())
                .internalPosition(employeeDto.getInternalPosition())
                .name(employeeDto.getName())
                .startDate(employeeDto.getStartDate())
                .supervisor(employeeDto.getSupervisor())
                .technologies(actualTechnologies)
                .unit(employeeDto.getUnit())
                .workingHours(employeeDto.getWorkingHours())
                .build();
    }

    private Set<TechnologyDto> mapTechnologiesToDtos(Set<Technology> technologies) {
        return technologies.stream()
                .map(t -> new TechnologyDto(t.getName(), t.getExternalId()))
                .collect(Collectors.toSet());
    }

    private Technology searchForTechnology(String externalID) {
        return technologyRepository.findByExternalId(externalID)
                .orElseThrow(() -> new TechnologyNotFoundByExternalIdException(externalID));
    }
}