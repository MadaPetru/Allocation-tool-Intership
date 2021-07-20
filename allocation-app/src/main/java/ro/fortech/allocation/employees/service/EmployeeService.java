package ro.fortech.allocation.employees.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employees.repository.EmployeeRepository;

import javax.validation.Valid;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeDto save(@Valid EmployeeDto employeeDto) {
        employeeDto.setUid(UUID.randomUUID().toString());
        return fromEntityToDto(employeeRepository.save(fromDtoToEntity(employeeDto)));
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

    public EmployeeDto fromEntityToDto(Employee employee) {
        return EmployeeDto.builder()
                .uid(employee.getUid())
                .active(employee.getActive())
                .businessUnit(employee.getBusinessUnit())
                .email(employee.getEmail())
                .endDate(employee.getEndDate())
                .internalPosition(employee.getInternalPosition())
                .name(employee.getName())
                .startDate(employee.getStartDate())
                .supervisor(employee.getSupervisor())
                .technicalExpertise(employee.getTechnicalExpertise())
                .unit(employee.getUnit())
                .workingHours(employee.getWorkingHours())
                .build();
    }

    public Employee fromDtoToEntity(EmployeeDto employeeDto) {
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
                .technicalExpertise(employeeDto.getTechnicalExpertise())
                .unit(employeeDto.getUnit())
                .workingHours(employeeDto.getWorkingHours())
                .build();
    }


}