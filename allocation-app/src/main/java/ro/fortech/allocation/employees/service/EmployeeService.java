package ro.fortech.allocation.employees.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employees.repository.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeDto save(EmployeeDto employeeDto){
        return fromEntityToDto(employeeRepository.save(fromDtoToEntity(employeeDto)));
    }

    public EmployeeDto update(EmployeeDto employeeDto , Long id){
        employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException(id));
        employeeDto.setId(id);
        return fromEntityToDto(employeeRepository.save(fromDtoToEntity(employeeDto)));
    }

    public EmployeeDto findById(Long id){
        return fromEntityToDto(employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException(id)));
    }
    public List<EmployeeDto> findAll(){
        return employeeRepository.findAll().stream().map(e->fromEntityToDto(e)).collect(Collectors.toList());
    }
    public void deleteById(Long id){
        employeeRepository.delete(employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException(id)));
    }
    public EmployeeDto fromEntityToDto(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
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
                .id(employeeDto.getId())
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
