package ro.fortech.allocation.technology.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.fortech.allocation.technology.dto.TechnologyDto;
import ro.fortech.allocation.technology.service.TechnologyService;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@CrossOrigin
public class TechnologyController implements TechnologyApi {
    private final TechnologyService service;

    @Override
    public ResponseEntity<TechnologyDto> add(TechnologyDto dto) {
        return new ResponseEntity<>(service.add(dto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<TechnologyDto> update(TechnologyDto dto, String externalId) {
        return new ResponseEntity<>(service.update(dto, externalId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<TechnologyDto>> getAllTechnologies(Pageable pageable) {
        return new ResponseEntity<>(service.findAll(pageable), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TechnologyDto> getTechnologyByExternalId(String externalId) {
        return new ResponseEntity<>(service.findByExternalId(externalId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Boolean> deleteTechnologyByExternalId(String externalId) {
        service.deleteByExternalId(externalId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TechnologyDto>> findTechnologiesByName(@RequestParam String name){
        return new ResponseEntity<>(service.findTechnologiesByName(name),HttpStatus.OK);
    }
}