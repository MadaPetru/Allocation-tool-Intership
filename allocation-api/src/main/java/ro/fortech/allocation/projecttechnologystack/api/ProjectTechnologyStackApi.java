package ro.fortech.allocation.projecttechnologystack.api;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.fortech.allocation.projecttechnologystack.dto.ProjectTechnologyStackDto;

@RequestMapping("/projectTechnology")
public interface ProjectTechnologyStackApi {
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add a projectTechnology")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ProjectTechnologyStack added"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    ResponseEntity<ProjectTechnologyStackDto> add(@RequestBody ProjectTechnologyStackDto dto);

    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete a projectTechnology")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ProjectTechnologyStack added"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    ResponseEntity<?> delete(@RequestBody ProjectTechnologyStackDto dto);
}
