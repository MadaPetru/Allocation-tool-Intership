package ro.fortech.allocation.employees.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.exception.EmployeeNotFoundException;
import ro.fortech.allocation.employees.service.EmployeeService;

import java.util.List;

@RequiredArgsConstructor(onConstructor=@__(@Autowired))
@RestController
public class EmployeeController implements EmployeeApi{
    private final EmployeeService employeeService;

    @Override
    public ResponseEntity<EmployeeDto> addEmployee(EmployeeDto employeeDto) {
        return new ResponseEntity<>(employeeService.save(employeeDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<EmployeeDto> findEmployeeById(Long employeeId) {
        try{
            return new ResponseEntity<>(employeeService.findById(employeeId),HttpStatus.OK);
        }
        catch (EmployeeNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Page<EmployeeDto>findAllEmployees(Pageable pageable) {
        return employeeService.findAll(pageable);//,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteEmployeeById(Long employeeId) {
        try{
            employeeService.deleteById(employeeId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (EmployeeNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<EmployeeDto> editEmployee(EmployeeDto employeeDto, Long employeeId) {
        try{
            return new ResponseEntity<>(employeeService.update(employeeDto,employeeId),HttpStatus.OK);
        }
        catch (EmployeeNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
