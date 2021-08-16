package ro.fortech.allocation.projecttechnologystack.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ro.fortech.allocation.projecttechnologystack.api.ProjectTechnologyStackApi;
import ro.fortech.allocation.projecttechnologystack.dto.ProjectTechnologyStackDto;
import ro.fortech.allocation.projecttechnologystack.service.ProjectTechnologyStackService;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectTechnologyStackController implements ProjectTechnologyStackApi {

    private final ProjectTechnologyStackService service;

    public ResponseEntity<ProjectTechnologyStackDto> add(ProjectTechnologyStackDto dto){
        return new ResponseEntity<ProjectTechnologyStackDto>(service.addProjectTechnology(dto), HttpStatus.CREATED);
    }

    public ResponseEntity<?> delete(ProjectTechnologyStackDto dto){
        service.deleteProjectTechnology(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}