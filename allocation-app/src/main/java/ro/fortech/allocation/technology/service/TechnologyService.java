package ro.fortech.allocation.technology.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employees.repository.EmployeeRepository;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.project.repository.ProjectRepository;
import ro.fortech.allocation.technology.dto.TechnologyDto;
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
    private final TechnologyRepository technologyRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    public TechnologyDto add(@Valid TechnologyDto dto) {
        String convertedName = dto.getName().toLowerCase().trim().replaceAll(" +", " ");
        Technology tech = this.dtoToTechnology(dto);
        tech.setName(convertedName);
        tech.setExternalId(UUID.randomUUID().toString());
        try {
            return this.technologyToDto(technologyRepository.save(tech));
        } catch (Exception e) {
            throw new TechnologyAlreadyExistsInTheDatabase(dto.getName(), tech.getName());
        }
    }

    public Page<TechnologyDto> findAll(Pageable pageable) {
        return technologyRepository.findAll(pageable).map(this::technologyToDto);
    }

    public TechnologyDto update(@Valid TechnologyDto dto, String externalId) {
        Technology technology = technologyRepository.findByExternalId(externalId).orElseThrow(() -> new TechnologyNotFoundByExternalIdException(externalId));
        String convertedName = dto.getName().toLowerCase().trim().replaceAll(" +", " ");
        Optional<Technology> tech = technologyRepository.findByName(convertedName);
        if (tech.isPresent()) {
            throw new TechnologyAlreadyExistsInTheDatabase(dto.getName(), tech.get().getName());
        } else {
            technology.setName(convertedName);
            technologyRepository.save(technology);
            return this.technologyToDto(technology);
        }
    }

    public TechnologyDto findByExternalId(String externalId) {
        Technology technology = technologyRepository.findByExternalId(externalId).orElseThrow(() -> new TechnologyNotFoundByExternalIdException(externalId));
        return technologyToDto(technology);
    }

    public boolean deleteByExternalId(String externalId) {
        Technology technology = technologyRepository.findByExternalId(externalId).orElseThrow(() -> new TechnologyNotFoundByExternalIdException(externalId));

        List<Employee> employees = getEmployeesWithTechnology(externalId);
        List<Project> projects = getProjectsWithTechnology(externalId);

        employees.forEach(employee ->
                eraseTechnologyForEmployee(technology, employee)
        );

        projects.forEach(project ->
                eraseTechnologyForProject(technology, project)
        );

        technologyRepository.delete(technology);

        return true;
    }

    private void eraseTechnologyForEmployee(Technology technology, Employee employee) {
        Set<Technology> set = employee.getTechnologies();
        Set<Technology> setResult = set.stream().filter(e -> !e.getName().equals(technology.getName()))
                .collect(Collectors.toSet());
        employee.setTechnologies(setResult);
        employeeRepository.save(employee);
    }

    private void eraseTechnologyForProject(Technology technology, Project project) {
        Set<Technology> set = project.getTechnologies();
        Set<Technology> setResult = set.stream()
                .filter(e -> !e.getName().equals(technology.getName()))
                .collect(Collectors.toSet());
        project.setTechnologies(setResult);
        projectRepository.save(project);
    }

    public List<TechnologyDto> findTechnologiesByName(String name) {
        return technologyRepository.findTechnologyByName(name)
                .stream()
                .map(this::technologyToDto)
                .collect(Collectors.toList());
    }

    public TechnologyDto technologyToDto(Technology technology) {
        TechnologyDto dto = new TechnologyDto();
        dto.setExternalId(technology.getExternalId());
        dto.setName(technology.getName());
        return dto;
    }

    public Technology dtoToTechnology(TechnologyDto dto) {
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

    private boolean employeeHasTechnology(Employee employee, String externalID) {
        Set<Technology> technologies = employee.getTechnologies();
        return technologies.stream()
                .map(Technology::getExternalId)
                .anyMatch(isEqual(externalID));
    }

    private boolean projectHasTechnology(Project project, String externalId) {
        Set<Technology> technologies = project.getTechnologies();
        return technologies.stream()
                .map(Technology::getExternalId)
                .anyMatch(isEqual(externalId));
    }
}