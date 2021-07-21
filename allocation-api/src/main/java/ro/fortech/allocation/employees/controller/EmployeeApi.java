package ro.fortech.allocation.employees.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;
import ro.fortech.allocation.employees.dto.EmployeeDto;

import java.util.List;

@RequestMapping("/employees")
public interface EmployeeApi {

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add new employee")
    @ApiResponses(value={
            @ApiResponse(code=201, message="Created employee"),
            @ApiResponse(code=400, message="Bad request")
    })
    ResponseEntity<EmployeeDto> addEmployee(@RequestBody EmployeeDto employeeDto);

    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Get employee by id")
    @ApiResponses(value={
            @ApiResponse(code=200, message="Success"),
            @ApiResponse(code=404, message="Employee not found")
    })
    @GetMapping(path="/{employeeId}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EmployeeDto> findEmployeeById(@PathVariable Long employeeId);

    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Get all employees")
    @ApiResponses(value={
            @ApiResponse(code=200, message="Success")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<EmployeeDto>> findAllEmployees();

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete an employee by id")
    @ApiResponses(value={
            @ApiResponse(code=204, message="Successfully deleted an employee"),
            @ApiResponse(code=404, message="Employee to be deleted not found")
    })
    @DeleteMapping(path="/{employeeId}")
    ResponseEntity<?> deleteEmployeeById(@PathVariable Long employeeId);

    @ResponseStatus(code=HttpStatus.OK)
    @PutMapping(path = "/{employeeId}" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value ="Update an employee")
    @ApiResponses(value={
            @ApiResponse(code=200, message="Successfully updated an employee"),
            @ApiResponse(code=400, message = "Bad request"),
            @ApiResponse(code=404, message="Employee to be updated not found")
    })
    ResponseEntity<EmployeeDto> editEmployee(@RequestBody EmployeeDto employeeDto,@PathVariable Long employeeId);

}
