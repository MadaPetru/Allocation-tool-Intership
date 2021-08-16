package ro.fortech.allocation.technology.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.fortech.allocation.technology.dto.TechnologyDto;

import java.util.List;

@RequestMapping("/technologies")
public interface TechnologyApi {
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add a technology")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Technology added"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    ResponseEntity<TechnologyDto> add(
            @RequestBody TechnologyDto technologyDto
    );

    @ResponseStatus(code = HttpStatus.OK)
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/{externalId}")
    @ApiOperation(value = "update a technology")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Technology updated"),
            @ApiResponse(code = 400, message = "invalid request"),
            @ApiResponse(code = 404, message = "Technology not found")
    })
    ResponseEntity<TechnologyDto> update(
            @RequestBody TechnologyDto technologyDto, @PathVariable String externalId
    );

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = {"/{externalId}"})
    @ApiOperation(value = "get technology by id")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "technology not found")
    })
    ResponseEntity<TechnologyDto> getTechnologyByExternalId(
            @PathVariable String externalId
    );


    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get all technologies")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Technology added"),
            @ApiResponse(code = 400, message = "invalid request")
    })
    ResponseEntity<Page<TechnologyDto>> getAllTechnologies(Pageable pageable);

    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(path = "/{externalId}")
    @ApiOperation(value = "delete technology")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Technology added"),
            @ApiResponse(code = 400, message = "invalid request")
    })
    ResponseEntity<?> deleteTechnologyByExternalId(
            @PathVariable String externalId
    );

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/find/" , produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get technologies by name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    ResponseEntity<List<TechnologyDto>> findTechnologiesByName(
            @RequestParam("name") String name
    );
}