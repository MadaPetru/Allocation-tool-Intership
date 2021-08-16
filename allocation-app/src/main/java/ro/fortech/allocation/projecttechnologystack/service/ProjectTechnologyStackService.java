package ro.fortech.allocation.projecttechnologystack.service;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ro.fortech.allocation.project.exception.ProjectNotFoundException;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.project.repository.ProjectRepository;
import ro.fortech.allocation.projecttechnologystack.dto.ProjectTechnologyStackDto;
import ro.fortech.allocation.projecttechnologystack.exception.ProjectTechnologyStackAlreadyExistException;
import ro.fortech.allocation.projecttechnologystack.exception.ProjectTechnologyStackNotFoundException;
import ro.fortech.allocation.projecttechnologystack.model.ProjectTechnologyStack;
import ro.fortech.allocation.projecttechnologystack.model.ProjectTechnologyStackKey;
import ro.fortech.allocation.projecttechnologystack.repository.ProjectTechnologyStackRepository;
import ro.fortech.allocation.technology.dto.TechnologyDto;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;
import ro.fortech.allocation.technology.model.Technology;
import ro.fortech.allocation.technology.repository.TechnologyRepository;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
@Validated
public class ProjectTechnologyStackService {
    private final TechnologyRepository technologyRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTechnologyStackRepository projectTechnologyStackRepository;
    private final ModelMapper mapper;

    public ProjectTechnologyStackDto addProjectTechnology(@Valid ProjectTechnologyStackDto dto){
        String externalIdProject = dto.getProjectExternalId();
        String externalIdTechnology = dto.getTechnologyExternalId();

        Project project = projectRepository.findProjectByExternalId(externalIdProject)
                .orElseThrow(() -> new ProjectNotFoundException(externalIdProject));

        Technology technology = technologyRepository.findByExternalId(externalIdTechnology)
                .orElseThrow(() -> new TechnologyNotFoundByExternalIdException(externalIdTechnology));

        ProjectTechnologyStack projectTechnologyStack = makeProjectTechnology(project,technology);

        projectTechnologyStack.setId(new ProjectTechnologyStackKey(project.getId(),technology.getId()));

        if(projectTechnologyStackRepository.existsByProjectAndTechnology(project, technology)){
            throw new ProjectTechnologyStackAlreadyExistException("Already exists !");
        }

        ProjectTechnologyStack saved = projectTechnologyStackRepository.save(projectTechnologyStack);
        return fromProjectTechnology_toProjectTechnologyDto(saved);

    }

    public void deleteProjectTechnology(@Valid  ProjectTechnologyStackDto projectTechnologyStackDto){
        Project project = projectRepository.findProjectByExternalId(projectTechnologyStackDto.getProjectExternalId())
                .orElseThrow(() -> new ProjectNotFoundException(projectTechnologyStackDto.getProjectExternalId()));

        Technology technology = technologyRepository.findByExternalId(projectTechnologyStackDto.getTechnologyExternalId())
                .orElseThrow(() -> new TechnologyNotFoundByExternalIdException(projectTechnologyStackDto.getTechnologyExternalId()));

        ProjectTechnologyStack projectTechnologyStack =projectTechnologyStackRepository.findByProjectAndTechnology(project,technology)
                .orElseThrow(() -> new ProjectTechnologyStackNotFoundException("Not found"));

        projectTechnologyStackRepository.delete(projectTechnologyStack);
    }

    private ProjectTechnologyStack makeProjectTechnology(Project project , Technology technology){
        ProjectTechnologyStack projectTechnologyStack = new ProjectTechnologyStack();
        projectTechnologyStack.setTechnology(technology);
        projectTechnologyStack.setProject(project);
        return projectTechnologyStack;
    }

    private ProjectTechnologyStackDto fromProjectTechnology_toProjectTechnologyDto(ProjectTechnologyStack projectTechnologyStack){
        ProjectTechnologyStackDto dto = new ProjectTechnologyStackDto();
        dto.setProjectExternalId(projectTechnologyStack.getProject().getExternalId());
        dto.setTechnologyExternalId(projectTechnologyStack.getTechnology().getExternalId());
        return dto;
    }

    private TechnologyDto fromTechnologyToTechnologyDto(Technology technology) {
        return mapper.map(technology,TechnologyDto.class);
    }
}