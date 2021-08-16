package ro.fortech.allocation.project.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.fortech.allocation.assignments.dto.ProjectAssignmentDto;
import ro.fortech.allocation.assignments.model.Assignment;
import ro.fortech.allocation.assignments.repository.AssignmentRepository;
import ro.fortech.allocation.employees.exception.CsvParseException;
import ro.fortech.allocation.project.helper.CsvHelper;
import ro.fortech.allocation.project.dto.ProjectAssignmentsDto;
import ro.fortech.allocation.project.dto.ProjectRequestDto;
import ro.fortech.allocation.project.dto.ProjectResponseDto;
import ro.fortech.allocation.project.exception.ProjectNotFoundException;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.project.repository.ProjectRepository;
import ro.fortech.allocation.technology.dto.TechnologyDto;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;
import ro.fortech.allocation.technology.repository.TechnologyRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectService {

    private final ModelMapper modelMapper;
    private final ProjectRepository projectRepository;
    private final AssignmentRepository assignmentRepository;
    private final TechnologyRepository technologyRepository;

    public Page<ProjectResponseDto> getProjects(Pageable pageable) {
        return projectRepository.findAll(pageable).map(this::toProjectResponseDto);
    }

    public void save(MultipartFile file) throws IOException {
        if(!CsvHelper.hasCSVFormat(file))
            throw new CsvParseException("");
        projectRepository.saveAll(CsvHelper.csvToProjects(file.getInputStream()));
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

        Project newProject = projectRequestDtoToProject(projectToUpdate);
        newProject.setId(project.getId());
        if(newProject.getExternalId()==null)
            newProject.setExternalId(project.getExternalId());
        projectRepository.save(newProject);
        return toProjectResponseDto(newProject);
    }

    public void deleteProject(String externalId) {
        projectRepository.delete(projectRepository.findProjectByExternalId(externalId).orElseThrow(() -> new ProjectNotFoundException(externalId)));
    }

    public ProjectAssignmentsDto getAssignmentsOfAProject(String externalId) {
        Project project = projectRepository.findProjectByExternalId(externalId).orElseThrow(() -> new ProjectNotFoundException(externalId));
        Set<Assignment> projectAssignments = assignmentRepository.findAssignmentByProject(project.getId());

        ProjectAssignmentsDto result = new ProjectAssignmentsDto();
        result.setProjectName(project.getName());
        result.setProjectExternalId(project.getExternalId());


        List<ProjectAssignmentDto> assignmentDtoList = new ArrayList<>();
        for (Assignment a : projectAssignments) {
            ProjectAssignmentDto temp = new ProjectAssignmentDto(
                    a.getUid(),
                    a.getEmployee().getName(),
                    a.getEmployee().getUid(),
                    a.getStartDate(),
                    a.getEndDate(),
                    a.getProjectPosition(),
                    a.getAllocationHours());
            assignmentDtoList.add(temp);
        }

        result.setAssignments(assignmentDtoList);
        return result;
    }

    private ProjectResponseDto toProjectResponseDto(Project project) {
        ProjectResponseDto result =  modelMapper.map(project, ProjectResponseDto.class);
        result.setTechnologyDtos(project.getTechnologies().stream().map(e->modelMapper.map(e, TechnologyDto.class)).collect(Collectors.toSet()));
        return result;
    }

    private Project projectRequestDtoToProject(ProjectRequestDto projectRequestDto) {
        Project project =  modelMapper.map(projectRequestDto, Project.class);
        project.setTechnologies(projectRequestDto.getTechnologyDtos().stream().map(e->technologyRepository.findByExternalId(e.getExternalId()).orElseThrow(()->new TechnologyNotFoundByExternalIdException(e.getExternalId()))).collect(Collectors.toSet()));
        return project;
    }
}