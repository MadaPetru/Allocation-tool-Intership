package ro.fortech.allocation.assignments.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.fortech.allocation.assignments.dto.AssignmentRequestDto;
import ro.fortech.allocation.assignments.dto.AssignmentResponseDto;

import java.text.ParseException;
import java.util.List;

@RequestMapping("/assignments")
public interface AssignmentApi {


    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add new assignment")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created assignment"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    ResponseEntity<AssignmentResponseDto> addAssignment(@RequestBody AssignmentRequestDto assignmentDto) throws ParseException;


    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{assignmentUid}")
    @ApiOperation(value = "Delete an assignment by Uid")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted an assignment"),
            @ApiResponse(code = 404, message = "Assignment to be deleted not found")
    })
    ResponseEntity<?> deleteAssignmentByUid(@PathVariable String assignmentUid);

    @ResponseStatus(code = HttpStatus.OK)
    @PutMapping(path = "/{assignmentUid}", consumes = MediaType.APPLICATION_JSON_VALUE)//path = "/{assignmentUid}"
    @ApiOperation(value = "Update an assignment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated an assignment"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Assignment to be updated not found")
    })
    ResponseEntity<AssignmentResponseDto> editAssignment(@RequestBody AssignmentRequestDto assignmentDto, @PathVariable String assignmentUid) throws ParseException;

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(path = "/validation", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Validate assignment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Assignment successfully validated"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    ResponseEntity<?> assignmentValidation(@RequestBody AssignmentRequestDto assignmentDto) throws ParseException;

    @GetMapping(path = "/project/{projectUid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Get all assignments with project")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Project for which assignments are searched was not found")
    })
    ResponseEntity<List<AssignmentResponseDto>> findAssignmentsByProject(@PathVariable String projectUid);
}
