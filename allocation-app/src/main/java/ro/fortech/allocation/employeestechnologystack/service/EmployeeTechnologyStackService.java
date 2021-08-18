package ro.fortech.allocation.employeestechnologystack.service;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employees.repository.EmployeeRepository;
import ro.fortech.allocation.employeestechnologystack.dto.EmployeeTechnologyStackDto;
import ro.fortech.allocation.employeestechnologystack.exception.EmployeeTechnologyNotFound;
import ro.fortech.allocation.employeestechnologystack.exception.EmployeeTechnologyStackAlreadyExist;
import ro.fortech.allocation.employeestechnologystack.model.EmployeeTechnologyStack;
import ro.fortech.allocation.employeestechnologystack.model.EmployeeTechnologyStackKey;
import ro.fortech.allocation.employeestechnologystack.repository.EmployeeTechnologyStackRepository;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;
import ro.fortech.allocation.technology.model.Technology;
import ro.fortech.allocation.technology.repository.TechnologyRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class EmployeeTechnologyStackService {
    private final TechnologyRepository technologyRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeTechnologyStackRepository repository;
    private final ModelMapper mapper;

    public EmployeeTechnologyStackDto addEmployeeTechnologyStack(@Valid EmployeeTechnologyStackDto dto) {
        String employeeExternalId = dto.getEmployeeUid();
        Employee employee = employeeRepository.findEmployeeByUid(employeeExternalId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeExternalId));
        String technologyExternalId = dto.getTechnologyExternalId();
        Technology technology = technologyRepository.findByExternalId(technologyExternalId)
                .orElseThrow(() -> new TechnologyNotFoundByExternalIdException(technologyExternalId));
        if (repository.findByTechnologyAndEmployee(technology, employee).isPresent()) {
            throw new EmployeeTechnologyStackAlreadyExist();
        }
        EmployeeTechnologyStack employeeTechnologyStack = fromDtoToEntity(dto);
        EmployeeTechnologyStack saved = repository.save(employeeTechnologyStack);
        return fromEntityToDto(saved);
    }

    public void deleteEmployeeTechnologyStack(@Valid EmployeeTechnologyStackDto dto) {
        String employeeExternalId = dto.getEmployeeUid();
        String technologyExternalId = dto.getTechnologyExternalId();

        Employee employee = employeeRepository.findEmployeeByUid(employeeExternalId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeExternalId));

        Technology technology = technologyRepository.findByExternalId(technologyExternalId)
                .orElseThrow(() -> new TechnologyNotFoundByExternalIdException(technologyExternalId));

        EmployeeTechnologyStack employeeTechnologyStack = repository.findByTechnologyAndEmployee(technology, employee)
                .orElseThrow(() -> new EmployeeTechnologyNotFound());

        repository.delete(employeeTechnologyStack);
    }

    public List<ro.fortech.allocation.technology.dto.TechnologyDto> getAllTechnologiesByEmployeeId(String employeeExternalId) {
        Employee employee = employeeRepository.findEmployeeByUid(employeeExternalId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeExternalId));
        List<ro.fortech.allocation.technology.dto.TechnologyDto> technologiesOfEmployee = new ArrayList<>();
        return technologiesOfEmployee;
    }

    private EmployeeTechnologyStack fromDtoToEntity(EmployeeTechnologyStackDto dto) {
        Employee employee = employeeRepository.findEmployeeByUid(dto.getEmployeeUid()).get();
        Technology technology = technologyRepository.findByExternalId(dto.getTechnologyExternalId()).get();
        EmployeeTechnologyStack employeeTechnologyStack = new EmployeeTechnologyStack();
        EmployeeTechnologyStackKey key = new EmployeeTechnologyStackKey();
        key.setEmployeeId(employee.getId());
        key.setTechnologyId(technology.getId());
        employeeTechnologyStack.setId(key);
        employeeTechnologyStack.setEmployee(employee);
        employeeTechnologyStack.setTechnology(technology);
        return employeeTechnologyStack;

    }

    private EmployeeTechnologyStackDto fromEntityToDto(EmployeeTechnologyStack entity) {
        return mapper.map(entity, EmployeeTechnologyStackDto.class);
    }

    private ro.fortech.allocation.technology.dto.TechnologyDto fromTechnologyToTechnologyDto(Technology technology) {
        return mapper.map(technology, ro.fortech.allocation.technology.dto.TechnologyDto.class);
    }
}
