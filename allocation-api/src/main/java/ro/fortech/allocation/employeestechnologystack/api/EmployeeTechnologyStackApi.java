package ro.fortech.allocation.employeestechnologystack.api;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.fortech.allocation.employeestechnologystack.dto.EmployeeTechnologyStackDto;
import ro.fortech.allocation.technology.dto.TechnologyDto;

import java.util.List;

@RequestMapping("/employeeTechnology")
public interface EmployeeTechnologyStackApi {
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add a employeeTechnology")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "EmployeeTechnologyStack added"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    ResponseEntity<EmployeeTechnologyStackDto> add(@RequestBody EmployeeTechnologyStackDto dto);

    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete a employeeTechnology")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "EmployeeTechnologyStack added"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    ResponseEntity<?> delete(@RequestBody EmployeeTechnologyStackDto dto);

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/{externalId}" , produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete employeeTechnology")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get employees"),
            @ApiResponse(code = 400, message = "invalid request")
    })
    ResponseEntity<List<TechnologyDto>> getTechnologiesByEmployeeId(@PathVariable(value = "externalId") String externalId);
}
