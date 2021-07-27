package ro.fortech.allocation.project.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.fortech.allocation.project.dto.ProjectRequestDto;
import ro.fortech.allocation.project.dto.ProjectResponseDto;

@RequestMapping("/projects")
public interface ProjectApi {
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Get all projects")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<ProjectResponseDto>> getProjects(Pageable pageable);

    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Get project by UUID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Project not found")
    })
    @GetMapping(path = "/{externalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProjectResponseDto> getProjectByExternalId(@PathVariable String externalId);

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add new project")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Project created"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @ResponseBody
    ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectRequestDto projectRequestDto);

    @ResponseStatus(code = HttpStatus.OK)
    @PutMapping(path = "/{externalId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update an employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Project has been successfully updated."),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Project to be updated not found.")
    })
    @ResponseBody
    ResponseEntity<ProjectResponseDto> updateProject(@PathVariable String externalId, @RequestBody ProjectRequestDto projectRequestDto);

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete an employee by externalId")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Project has been successfully deleted."),
            @ApiResponse(code = 404, message = "Project to be deleted not found.")
    })
    @DeleteMapping(path = "/{externalId}")
    ResponseEntity<?> deleteProject(@PathVariable String externalId);
}
