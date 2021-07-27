package ro.fortech.allocation.project.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.fortech.allocation.project.dto.ProjectRequestDto;
import ro.fortech.allocation.project.dto.ProjectResponseDto;
import ro.fortech.allocation.project.exception.ProjectNotFoundException;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.project.repository.ProjectRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectService {

    private final ModelMapper modelMapper;
    private final ProjectRepository projectRepository;

    public Page<ProjectResponseDto> getProjects(Pageable pageable) {
        return projectRepository.findAll(pageable).map(this::toProjectResponseDto);
    }

    public ProjectResponseDto getProjectByExternalId(String externalId) {
        Project project = projectRepository.findProjectByExternalId(externalId).orElseThrow(() -> new ProjectNotFoundException(externalId));
        return this.toProjectResponseDto(project);
    }

    public ProjectResponseDto createProject(ProjectRequestDto projectRequestDto) {
        Project project = this.projectRequestDtoToProject(projectRequestDto);

        project.setExternalId(UUID.randomUUID().toString());
        projectRepository.save(project);
        return this.toProjectResponseDto(project);
    }

    public ProjectResponseDto updateProject(String externalId, ProjectRequestDto projectToUpdate) {
        Project project = projectRepository.findProjectByExternalId(externalId).orElseThrow(() -> new ProjectNotFoundException(externalId));

        project.setName(projectToUpdate.getName());
        project.setClient(projectToUpdate.getClient());
        project.setStartDate(projectToUpdate.getStartDate());
        project.setEndDate(projectToUpdate.getEndDate());
        project.setDescription(projectToUpdate.getDescription());
        project.setTechnicalStack(projectToUpdate.getTechnicalStack());

        projectRepository.save(project);
        return toProjectResponseDto(project);
    }

    public void deleteProject(String externalId) {
        projectRepository.delete(projectRepository.findProjectByExternalId(externalId).orElseThrow(() -> new ProjectNotFoundException(externalId)));
    }

    private ProjectResponseDto toProjectResponseDto(Project project) {
        return modelMapper.map(project, ProjectResponseDto.class);
    }

    private Project projectRequestDtoToProject(ProjectRequestDto projectRequestDto) {
        return modelMapper.map(projectRequestDto, Project.class);
    }
}
