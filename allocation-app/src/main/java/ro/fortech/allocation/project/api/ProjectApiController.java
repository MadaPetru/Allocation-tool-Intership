package ro.fortech.allocation.project.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.fortech.allocation.project.dto.ProjectRequestDto;
import ro.fortech.allocation.project.dto.ProjectResponseDto;
import ro.fortech.allocation.project.service.ProjectService;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "projects")
public class ProjectApiController {
    private  final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getProjects(){
        List<ProjectResponseDto> projectResponsDtos = projectService.getAllProjects();
        return ResponseEntity.ok(projectResponsDtos);
    }

    @GetMapping(path = "{externalId}")
    private  ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable("externalId") @Valid String externalId) {
        ProjectResponseDto result = projectService.getProjectById(externalId);
        return  ResponseEntity.ok(result);
    }

    @PostMapping
    private ResponseEntity<ProjectResponseDto> saveUser(@RequestBody @Valid ProjectRequestDto projectRequestDto) {
        ProjectResponseDto result = projectService.createProject(projectRequestDto);
        return ResponseEntity.ok(result);
    }

    @PutMapping(path = "{externalId}")
    private ResponseEntity<ProjectResponseDto>updateProject(@PathVariable("externalId") @Valid String externalId, @RequestBody @Valid ProjectRequestDto projectRequestDto) {
        ProjectResponseDto result  = projectService.updateProject(externalId,  projectRequestDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(path = "{externalId}")
    private ResponseEntity<?> deleteProject(@PathVariable("externalId") @Valid String externalId) {
        projectService.deleteProject(externalId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
