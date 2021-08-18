package ro.fortech.allocation.technology.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ro.fortech.allocation.employees.repository.EmployeeRepository;
import ro.fortech.allocation.project.repository.ProjectRepository;
import ro.fortech.allocation.technology.dto.TechnologyDto;
import ro.fortech.allocation.technology.exception.TechnologyAlreadyExistsInTheDatabase;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;
import ro.fortech.allocation.technology.model.Technology;
import ro.fortech.allocation.technology.repository.TechnologyRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TechnologyServiceTest {
    @InjectMocks
    private TechnologyService service;

    @Mock
    private TechnologyRepository repository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Test
    public void findTechnologiesByName_givenName_expectTechnologies() {
        Technology tech = Technology.builder()
                .externalId("externalId")
                .id(1L)
                .name("tech")
                .build();
        List<Technology> list = new LinkedList<>();
        list.add(tech);
        when(repository.findTechnologyByName(any(String.class))).thenReturn(list);
        service.findTechnologiesByName(tech.getName());
        verify(repository).findTechnologyByName(tech.getName());
        assertEquals(1, service.findTechnologiesByName(tech.getName()).size());
    }

    @Test
    public void addTechnology_givenTechnology_expectTechnology() {
        Technology tech = Technology.builder()
                .name("tech")
                .externalId("externalId")
                .build();
        TechnologyDto dto = TechnologyDto.builder()
                .name("tech")
                .externalId("externalId")
                .build();

        when(repository.save(any(Technology.class))).thenReturn(tech);
        TechnologyDto savedTech = service.add(dto);
        dto.setExternalId(savedTech.getExternalId());
        verify(repository).save(any(Technology.class));
        assertThat(savedTech.getName()).isEqualTo(dto.getName());
        assertThat(savedTech.getExternalId()).isEqualTo(dto.getExternalId());
    }

    @Test
    public void findAllTechnology_givenTechnologies_expectTechnologies() {
        Technology tech = Technology.builder()
                .name("tech")
                .externalId("externalId")
                .build();
        TechnologyDto dto = TechnologyDto.builder()
                .name("tech")
                .externalId("externalId")
                .build();
        Pageable page = PageRequest.of(3, 3);
        Page<Technology> pageTech = new PageImpl<>(Collections.singletonList(tech));
        when(repository.findAll(page)).thenReturn(pageTech);
        Page<TechnologyDto> pageResult = service.findAll(page);
        assertEquals(pageResult.getTotalElements(), pageTech.getTotalElements());
    }

    @Test
    public void updateTechnology_givenTechnology_expectedTechnology() {
        Technology tech = Technology.builder()
                .name("tech")
                .externalId("externalId")
                .build();
        TechnologyDto dto = TechnologyDto.builder()
                .name("tech")
                .externalId("externalId")
                .build();
        when(repository.findByExternalId(any(String.class))).thenReturn(java.util.Optional.ofNullable(tech));
        when(repository.save(any(Technology.class))).thenReturn(tech);

        TechnologyDto updatedTech = service.update(dto, dto.getExternalId());
        verify(repository).save(any(Technology.class));
        assertThat(updatedTech.getName()).isEqualTo(dto.getName());
        assertThat(updatedTech.getExternalId()).isEqualTo(dto.getExternalId());
    }

    @Test
    public void findByExternalId_givenExternalId_expectedTechnology() {
        Technology tech = Technology.builder()
                .name("tech")
                .externalId("externalId")
                .build();
        TechnologyDto dto = TechnologyDto.builder()
                .name("tech")
                .externalId("externalId")
                .build();
        when(repository.findByExternalId(any(String.class))).thenReturn(java.util.Optional.ofNullable(tech));
        TechnologyDto foundedTech = service.findByExternalId(dto.getExternalId());
        verify(repository).findByExternalId(dto.getExternalId());
        assertThat(foundedTech.getExternalId()).isEqualTo(dto.getExternalId());
        assertThat(foundedTech.getName()).isEqualTo(dto.getName());
    }

    @Test
    public void deleteByExternalId_givenExternalId_ExpectedTrue() {
        Technology tech = Technology.builder()
                .name("tech")
                .externalId("externalId")
                .build();
        TechnologyDto dto = TechnologyDto.builder()
                .name("tech")
                .externalId("externalId")
                .build();

        when(repository.findByExternalId(tech.getExternalId())).thenReturn(java.util.Optional.of(tech));
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());
        when(projectRepository.findAll()).thenReturn(new ArrayList<>());

        Boolean expected = service.deleteByExternalId(dto.getExternalId());
        verify(repository).delete(tech);
        assertEquals(true, expected);
    }

    @Test
    public void technologyToDtoTest() {
        Technology tech = Technology.builder()
                .name("tech")
                .externalId("externalId")
                .build();
        TechnologyDto dto = service.technologyToDto(tech);
        assertThat(dto.getName()).isEqualTo(tech.getName());
        assertThat(dto.getExternalId()).isEqualTo(tech.getExternalId());
    }

    @Test
    public void dtoToTechnologyTest() {
        TechnologyDto dto = TechnologyDto.builder()
                .name("tech")
                .externalId("externalId")
                .build();
        Technology tech = service.dtoToTechnology(dto);
        assertThat(tech.getName()).isEqualTo(dto.getName());
        assertThat(tech.getExternalId()).isEqualTo(dto.getExternalId());
    }

    @Test(expected = TechnologyNotFoundByExternalIdException.class)
    public void findByExternalId_givenExternalId_expectedTechnologyNotFoundByExternalIdException() {
        TechnologyDto dto = makeDto();
        dto.setExternalId("tech");
        when(repository.findByExternalId(any(String.class))).thenReturn(Optional.empty());
        service.findByExternalId(dto.getExternalId());
    }

    @Test(expected = TechnologyNotFoundByExternalIdException.class)
    public void deleteByExternalId_givenExternalId_expectedTechnologyNotFoundByExternalIdException() {
        Technology tech = makeTechnology();
        tech.setExternalId("tech");
        when(repository.findByExternalId(any(String.class))).thenReturn(Optional.empty());
        service.deleteByExternalId(tech.getExternalId());
    }

    @Test(expected = TechnologyAlreadyExistsInTheDatabase.class)
    public void updateTechnology_givenTechnology_expectTechnologyAlreadyExistInDatabase() {
        Technology tech = makeTechnology();
        TechnologyDto dto = makeDto();
        dto.setExternalId("tech");
        tech.setExternalId("tech");
        when(repository.findByExternalId(any(String.class))).thenReturn(Optional.of(tech));
        when(repository.findByName(any(String.class))).thenReturn(Optional.of(tech));
        service.update(dto, dto.getExternalId());
    }

    @Test(expected = TechnologyNotFoundByExternalIdException.class)
    public void updateTechnology_givenTechnology_expectTechnologyNotFoundByExternalId() {
        TechnologyDto dto = makeDto();
        dto.setExternalId("tech");
        when(repository.findByExternalId(any(String.class))).thenReturn(Optional.empty());
        service.update(dto, dto.getExternalId());
    }

    @Test(expected = TechnologyAlreadyExistsInTheDatabase.class)
    public void addTechnology_givenTechnology_expectTechnologyAlreadyExistInTheDatabase() {
        TechnologyDto request = makeDto();
        when(repository.save(any(Technology.class))).thenThrow(TechnologyAlreadyExistsInTheDatabase.class);
        service.add(request);
    }

    private TechnologyDto makeDto() {
        return TechnologyDto.builder()
                .name("tech")
                .build();
    }

    private Technology makeTechnology() {
        return Technology.builder()
                .name("tech")
                .build();
    }
}