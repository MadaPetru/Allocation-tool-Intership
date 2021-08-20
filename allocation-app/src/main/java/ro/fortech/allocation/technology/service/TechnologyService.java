package ro.fortech.allocation.technology.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ro.fortech.allocation.assignments.repository.AssignmentRepository;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employees.repository.EmployeeRepository;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.project.repository.ProjectRepository;
import ro.fortech.allocation.technology.exception.TechnologyAlreadyExistsInTheDatabase;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;
import ro.fortech.allocation.technology.model.Technology;
import ro.fortech.allocation.technology.repository.TechnologyRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.function.Predicate.isEqual;

@Service
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TechnologyService {
    private final TechnologyRepository repository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final AssignmentRepository assignmentRepository;

    public ro.fortech.allocation.technology.dto.TechnologyDto add(@Valid ro.fortech.allocation.technology.dto.TechnologyDto dto) {
        String convertedName = dto.getName().toLowerCase().trim().replaceAll(" +", " ");
        Technology tech = this.dtoToTechnology(dto);
        tech.setName(convertedName);
        tech.setExternalId(UUID.randomUUID().toString());
        try {
            return this.technologyToDto(repository.save(tech));
        } catch (Exception e) {
            throw new TechnologyAlreadyExistsInTheDatabase(dto.getName(), tech.getName());
        }
    }

    public Page<ro.fortech.allocation.technology.dto.TechnologyDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::technologyToDto);
    }

    public ro.fortech.allocation.technology.dto.TechnologyDto update(@Valid ro.fortech.allocation.technology.dto.TechnologyDto dto, String externalId) {
        Technology technology = repository.findByExternalId(externalId).orElseThrow(() -> new TechnologyNotFoundByExternalIdException(externalId));
        String convertedName = dto.getName().toLowerCase().trim().replaceAll(" +", " ");
        Optional<Technology> tech = repository.findByName(convertedName);
        if (tech.isPresent()) {
            throw new TechnologyAlreadyExistsInTheDatabase(dto.getName(), tech.get().getName());
        } else {
            technology.setName(convertedName);
            repository.save(technology);
            return this.technologyToDto(technology);
        }
    }

    public ro.fortech.allocation.technology.dto.TechnologyDto findByExternalId(String externalId) {
        Technology technology = repository.findByExternalId(externalId).orElseThrow(() -> new TechnologyNotFoundByExternalIdException(externalId));
        return technologyToDto(technology);
    }

    public boolean deleteByExternalId(String externalId) {
        Technology technology = repository.findByExternalId(externalId).orElseThrow(() -> new TechnologyNotFoundByExternalIdException(externalId));

        List<Employee> employees = getEmployeesWithTechnology(externalId);
        List<Project> projects = getProjectsWithTechnology(externalId);

        deleteAllAssignmentsWithProjectsOrEmployees(employees, projects);
        employeeRepository.deleteAll(employees);
        projectRepository.deleteAll(projects);

        repository.delete(technology);

        return true;
    }

    public List<ro.fortech.allocation.technology.dto.TechnologyDto> findTechnologiesByName(String name) {

        return repository.findTechnologyByName(name)
                .stream()
                .map(this::technologyToDto)
                .collect(Collectors.toList());
    }

    public ro.fortech.allocation.technology.dto.TechnologyDto technologyToDto(Technology technology) {
        ro.fortech.allocation.technology.dto.TechnologyDto dto = new ro.fortech.allocation.technology.dto.TechnologyDto();
        dto.setExternalId(technology.getExternalId());
        dto.setName(technology.getName());
        return dto;
    }

    public Technology dtoToTechnology(ro.fortech.allocation.technology.dto.TechnologyDto dto) {
        Technology technology = new Technology();
        technology.setExternalId(dto.getExternalId());
        technology.setName(dto.getName());
        return technology;
    }

    private List<Project> getProjectsWithTechnology(String externalId) {
        return projectRepository.findAll().stream()
                .filter(project -> projectHasTechnology(project, externalId))
                .collect(Collectors.toList());
    }

    private List<Employee> getEmployeesWithTechnology(String externalId) {
        return employeeRepository.findAll().stream()
                .filter(employee -> employeeHasTechnology(employee, externalId))
                .collect(Collectors.toList());
    }

    private void deleteAllAssignmentsWithProjectsOrEmployees(List<Employee> employees, List<Project> projects) {
        for (Employee employee : employees) {
            assignmentRepository.deleteAll(assignmentRepository.findAssignmentsByEmployee(employee));
        }

        for (Project project : projects) {
            assignmentRepository.deleteAll(assignmentRepository.findAssignmentsByProject(project));
        }
    }

    private boolean employeeHasTechnology(Employee employee, String externalID) {
        Set<Technology> technologies = employee.getTechnologies();
        boolean hasTechnology = technologies.stream()
                .map(Technology::getExternalId)
                .anyMatch(isEqual(externalID));
        return hasTechnology;
    }

    private boolean projectHasTechnology(Project project, String externalId) {
        Set<Technology> technologies = project.getTechnologies();
        boolean hasTechnology = technologies.stream()
                .map(Technology::getExternalId)
                .anyMatch(isEqual(externalId));
        return hasTechnology;
    }
}