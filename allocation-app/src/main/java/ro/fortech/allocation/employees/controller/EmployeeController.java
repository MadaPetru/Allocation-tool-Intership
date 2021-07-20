package ro.fortech.allocation.employees.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.service.EmployeeService;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@CrossOrigin
public class EmployeeController implements EmployeeApi {
    private final EmployeeService employeeService;

    @Override
    public ResponseEntity<Page<EmployeeDto>> findAllEmployees(Pageable pageable) {

        return new ResponseEntity<>(employeeService.findAll(pageable), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EmployeeDto> addEmployee(EmployeeDto employeeDto) {
        return new ResponseEntity<>(employeeService.save(employeeDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<EmployeeDto> findEmployeeByUid(String employeeUid) {
        return new ResponseEntity<>(employeeService.findByUid(employeeUid), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteEmployeeByUid(@PathVariable String employeeUid) {
        employeeService.deleteByUid(employeeUid);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Override
    public ResponseEntity<EmployeeDto> editEmployee(EmployeeDto employeeDto, String employeeUid) {
        return new ResponseEntity<>(employeeService.update(employeeDto, employeeUid), HttpStatus.OK);
    }
}