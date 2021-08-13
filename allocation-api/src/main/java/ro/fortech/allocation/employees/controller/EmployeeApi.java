package ro.fortech.allocation.employees.controller;

import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.fortech.allocation.employees.dto.EmployeeDto;

@RequestMapping("/employees")
public interface EmployeeApi {

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all employees")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    ResponseEntity<Page<EmployeeDto>> findAllEmployees(Pageable pageable);


    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add new employee")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created employee"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    ResponseEntity<EmployeeDto> addEmployee(@RequestBody EmployeeDto employeeDto);

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/{employeeUid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get employee by Uid")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Employee not found")
    })
    ResponseEntity<EmployeeDto> findEmployeeByUid(@PathVariable String employeeUid);

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{employeeUid}")
    @ApiOperation(value = "Delete an employee by Uid")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted an employee"),
            @ApiResponse(code = 404, message = "Employee to be deleted not found")
    })
    ResponseEntity<?> deleteEmployeeByUid(@PathVariable String employeeUid);

    @ResponseStatus(code = HttpStatus.OK)
    @PutMapping(path = "/{employeeUid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update an employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated an employee"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Employee to be updated not found")
    })
    ResponseEntity<EmployeeDto> editEmployee(@RequestBody EmployeeDto employeeDto, @PathVariable String employeeUid);

}