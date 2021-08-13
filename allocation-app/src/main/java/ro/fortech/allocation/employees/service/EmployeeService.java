package ro.fortech.allocation.employees.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

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
                .uid(employee.getUid())
                .email(employee.getEmail())
                .build();
    }
    private EmployeeDto fromEntityToDto(Employee employee) {
        return modelMapper.map(employee, EmployeeDto.class);
    }

    private Employee fromDtoToEntity(EmployeeDto employeeDto) {
        return modelMapper.map(employeeDto, Employee.class);
    }


}