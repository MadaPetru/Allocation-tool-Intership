package ro.fortech.allocation.project.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.fortech.allocation.project.controller.ProjectApi;
import ro.fortech.allocation.project.dto.ProjectAssignmentsDto;
import ro.fortech.allocation.project.dto.ProjectRequestDto;
import ro.fortech.allocation.project.dto.ProjectResponseDto;
import ro.fortech.allocation.project.service.ProjectService;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectController implements ProjectApi {
    private final ProjectService projectService;

    @Override
    public ResponseEntity<Page<ProjectResponseDto>> getProjects(Pageable pageable) {
        return ResponseEntity.ok(projectService.getProjects(pageable));
    }

    @Override
    public ProjectAssignmentsDto getProjectAssignments(@PathVariable("externalId") @Valid String externalId) {
        return projectService.getAssignmentsOfAProject(externalId);
    }

    @Override
    public ResponseEntity<ProjectResponseDto> getProjectByExternalId(@PathVariable("externalId") @Valid String externalId) {
        ProjectResponseDto result = projectService.getProjectByExternalId(externalId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody @Valid ProjectRequestDto projectRequestDto) {
        ProjectResponseDto result = projectService.createProject(projectRequestDto);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable("externalId") @Valid String externalId, @RequestBody @Valid ProjectRequestDto projectRequestDto) {
        return ResponseEntity.ok(projectService.updateProject(externalId, projectRequestDto));
    }

    @Override
    public ResponseEntity<?> deleteProject(@PathVariable("externalId") @Valid String externalId) {
        projectService.deleteProject(externalId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> uploadFile(MultipartFile file) throws IOException {
        projectService.save(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProjectResponseDto> addTechnologyToProject(@PathVariable String projectUid, @RequestParam String technologyUid) {
        return ResponseEntity.ok(projectService.addTechnologyToProject(projectUid, technologyUid));
    }
}