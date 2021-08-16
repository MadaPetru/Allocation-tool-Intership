package ro.fortech.allocation.technology.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ro.fortech.allocation.technology.dto.TechnologyDto;
import ro.fortech.allocation.technology.exception.TechnologyAlreadyExistsInTheDatabase;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;
import ro.fortech.allocation.technology.model.Technology;
import ro.fortech.allocation.technology.repository.TechnologyRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TechnologyService {
    private final TechnologyRepository repository;

    public TechnologyDto add(@Valid TechnologyDto dto) {
        String convertedName = dto.getName().toLowerCase().trim().replaceAll(" +"," ");
            Technology tech = this.dtoToTechnology(dto);
            tech.setName(convertedName);
            tech.setExternalId(UUID.randomUUID().toString());
            try {
                return this.technologyToDto(repository.save(tech));
            } catch (Exception e) {
                throw new TechnologyAlreadyExistsInTheDatabase(dto.getName(), tech.getName());
            }
        }

    public Page<TechnologyDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::technologyToDto);
    }

    public TechnologyDto update(@Valid  TechnologyDto dto,  String externalId) {
        Technology technology = repository.findByExternalId(externalId).orElseThrow(() -> new TechnologyNotFoundByExternalIdException(externalId));
        String convertedName = dto.getName().toLowerCase().trim().replaceAll(" +"," ");
        Optional<Technology> tech = repository.findByName(convertedName);
        if (tech.isPresent()) {
            throw new TechnologyAlreadyExistsInTheDatabase(dto.getName(), tech.get().getName());
        } else {
            technology.setName(convertedName);
            repository.save(technology);
            return this.technologyToDto(technology);
        }
    }

    public TechnologyDto findByExternalId(String externalId) {
        Technology technology = repository.findByExternalId(externalId).orElseThrow(() -> new TechnologyNotFoundByExternalIdException(externalId));
        return technologyToDto(technology);
    }

    public boolean deleteByExternalId(String externalId) {
        Technology technology = repository.findByExternalId(externalId).orElseThrow(() -> new TechnologyNotFoundByExternalIdException(externalId));
        repository.delete(technology);
        return true;
    }

    public List<TechnologyDto> findTechnologiesByName(String name){

       return repository.findTechnologyByName(name)
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
}