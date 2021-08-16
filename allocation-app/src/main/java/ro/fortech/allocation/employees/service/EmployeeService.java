package ro.fortech.allocation.employees.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.dto.EmployeeEmailDto;
import ro.fortech.allocation.employees.exception.CsvParseException;
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;
import ro.fortech.allocation.employees.helper.CsvHelper;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employees.repository.EmployeeRepository;
import ro.fortech.allocation.technology.model.Technology;
import ro.fortech.allocation.technology.repository.TechnologyRepository;

import javax.persistence.EntityNotFoundException;
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

    public EmployeeDto save(@Valid EmployeeDto employeeDto) {
        employeeDto.setUid(UUID.randomUUID().toString());
        return fromEntityToDto(employeeRepository.save(fromDtoToEntity(employeeDto)));
    }

    public void save(MultipartFile file) throws IOException {
        if(!CsvHelper.hasCSVFormat(file))
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
        employeeRepository.delete(employeeRepository.findEmployeeByUid(uid).orElseThrow(() -> new EmployeeNotFoundException(uid)));
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

        Set<String> technologiesNames = mapTechnologiesIntoStrings(technologies);

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
                .technologies(technologiesNames)
                .build();
    }

    public Employee fromDtoToEntity(EmployeeDto employeeDto) {

        Set<String> technologiesNames = employeeDto.getTechnologies();

        Set<Technology> actualTechnologies = technologiesNames.stream().map(this::searchForTechnology)
                .collect(Collectors.toSet());

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

    private Set<String> mapTechnologiesIntoStrings(Set<Technology> technologies) {
        return technologies.stream()
                .map(Technology::getName)
                .collect(Collectors.toSet());
    }

    private Technology searchForTechnology(String technology) {
        return technologyRepository.findByName(technology)
                .orElseThrow(() -> new EntityNotFoundException("Couldn't find technology with name: " + technology + "!"));
    }
}