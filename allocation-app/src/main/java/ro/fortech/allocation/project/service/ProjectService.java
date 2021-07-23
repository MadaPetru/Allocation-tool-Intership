package ro.fortech.allocation.project.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.fortech.allocation.project.dto.ProjectRequestDto;
import ro.fortech.allocation.project.dto.ProjectResponseDto;
import ro.fortech.allocation.project.repository.ProjectRepository;
import ro.fortech.allocation.project.model.Project;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectService {

    private final ModelMapper modelMapper;
    private final ProjectRepository projectRepository;

    public  ProjectResponseDto getProjectByExternalId(String externalId){
        Project project = projectRepository.findProjectByExternalId(externalId).orElseThrow(() -> new IllegalStateException("Project with projectId " + externalId + " was not found!"));
        return this.toProjectResponseDto(project);
    }

    public ProjectResponseDto createProject(ProjectRequestDto projectRequestDto) {
        Project project = this.ProjectRequestDtoToProject(projectRequestDto);

        project.setExternalId( UUID.randomUUID().toString());
        projectRepository.save(project);
        return  this.toProjectResponseDto(project);
    }

    @Transactional
    public ProjectResponseDto updateProject(String externalId, ProjectRequestDto projectToUpdate) {
        Project project = projectRepository.findProjectByExternalId(externalId).orElseThrow(() -> new IllegalStateException("Project with id " + externalId + " was not found!"));

        project.setName(projectToUpdate.getName());
        project.setClient(projectToUpdate.getClient());
        project.setStartDate(projectToUpdate.getStartDate());
        project.setEndDate(projectToUpdate.getEndDate());
        project.setDescription(projectToUpdate.getDescription());
        project.setTechnicalStack(projectToUpdate.getTechnicalStack());

        projectRepository.save(project);
        return toProjectResponseDto(project);
    }

    @Transactional
    public  void deleteProject(String externalId){
        boolean exists = projectRepository.existsProjectByExternalId(externalId);
        if(!exists) {
            throw new IllegalStateException("Project with id " + externalId + " does not exist!");
        }
        projectRepository.deleteProjectByExternalId(externalId);
    }

    public ProjectResponseDto toProjectResponseDto(Project project) {
        return modelMapper.map(project, ProjectResponseDto.class);
    }

    public ProjectRequestDto toProjectRequestDto(Project project) {
        return modelMapper.map(project, ProjectRequestDto.class);
    }

    public  Project ProjectRequestDtoToProject(ProjectRequestDto  projectRequestDto) {
        return modelMapper.map(projectRequestDto, Project.class);
    }

    public  Project ProjectResponseDtoToProject(ProjectResponseDto  projectResponseDto) {
        return modelMapper.map(projectResponseDto, Project.class);
    }

    public Page<ProjectResponseDto> getProjects(Pageable pageable) {
        return projectRepository.findAll(pageable).map(this::toProjectResponseDto);
    }
}
