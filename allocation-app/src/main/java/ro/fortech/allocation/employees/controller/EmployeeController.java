package ro.fortech.allocation.employees.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.dto.EmployeeEmailDto;
import ro.fortech.allocation.employees.service.EmployeeService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

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
    public ResponseEntity<List<EmployeeEmailDto>> findEmployeesByEmail(@RequestParam @Valid String email) {
        return ResponseEntity.ok(employeeService.findEmployeeByEmail(email));
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

    @Override
    public ResponseEntity<?> uploadFile(MultipartFile file) throws IOException {
        employeeService.save(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EmployeeDto> addTechnologyToEmployee(String employeeUid, String externalId) {
        return ResponseEntity.ok(employeeService.addTechnologyToEmployee(employeeUid, externalId));
    }
}