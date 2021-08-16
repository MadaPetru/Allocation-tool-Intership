package ro.fortech.allocation.employeestechnologystack.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ro.fortech.allocation.employeestechnologystack.api.EmployeeTechnologyStackApi;
import ro.fortech.allocation.employeestechnologystack.dto.EmployeeTechnologyStackDto;
import ro.fortech.allocation.employeestechnologystack.service.EmployeeTechnologyStackService;
import ro.fortech.allocation.technology.dto.TechnologyDto;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@CrossOrigin
public class EmployeeTechnologyStackController implements EmployeeTechnologyStackApi {

    private final EmployeeTechnologyStackService service;

    @Override
    public ResponseEntity<EmployeeTechnologyStackDto> add(EmployeeTechnologyStackDto dto) {
        EmployeeTechnologyStackDto result = service.addEmployeeTechnologyStack(dto);
        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> delete(EmployeeTechnologyStackDto dto) {
        service.deleteEmployeeTechnologyStack(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TechnologyDto>> getTechnologiesByEmployeeId(String employeeExternalId) {
        return new ResponseEntity<>(service.getAllTechnologiesByEmployeeId(employeeExternalId) , HttpStatus.OK);
    }
}
