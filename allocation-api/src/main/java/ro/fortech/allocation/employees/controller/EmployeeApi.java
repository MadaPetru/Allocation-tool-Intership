package ro.fortech.allocation.employees.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.dto.EmployeeEmailDto;

import java.io.IOException;
import java.util.List;

@RequestMapping("/employees")
public interface EmployeeApi {

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all employees")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    ResponseEntity<Page<EmployeeDto>> findAllEmployees(Pageable pageable);


    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Get all employees with email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    ResponseEntity<List<EmployeeEmailDto>> findEmployeesByEmail(@RequestParam String email);


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

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(path = "/upload")
    @ApiOperation(value = "Save employees from CSV file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully saved employees"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException;

    @ResponseStatus(code = HttpStatus.OK)
    @PatchMapping(path = "/{employeeUid}")
    @ApiOperation(value = "Add a technology to an employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added technology to the employee"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    ResponseEntity<EmployeeDto> addTechnologyToEmployee(@PathVariable String employeeUid, @RequestParam String technologyUid);
}