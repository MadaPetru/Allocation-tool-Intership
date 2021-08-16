package ro.fortech.allocation.employeestechnologystack.repository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employees.repository.EmployeeRepository;
import ro.fortech.allocation.employeestechnologystack.model.EmployeeTechnologyStack;
import ro.fortech.allocation.employeestechnologystack.model.EmployeeTechnologyStackKey;
import ro.fortech.allocation.technology.model.Technology;
import ro.fortech.allocation.technology.repository.TechnologyRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;


@RunWith(SpringRunner.class)
@DataJpaTest
class EmployeeTechnologyStackRepositoryTest {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    TechnologyRepository technologyRepository;
    @Autowired
    EmployeeTechnologyStackRepository repository;

    @Test
    void findByTechnologyAndEmployee() throws ParseException {
        EmployeeTechnologyStack employeeTechnologyStack = makeEntity();
        Employee employee = employeeTechnologyStack.getEmployee();
        Employee save = employeeRepository.save(employee);
        Technology technology = employeeTechnologyStack.getTechnology();
        technologyRepository.save(technology);
        EmployeeTechnologyStackKey key = new EmployeeTechnologyStackKey();
        key.setTechnologyId(technology.getId());
        key.setEmployeeId(employee.getId());
        employeeTechnologyStack.setId(key);
        repository.save(employeeTechnologyStack);
        Optional<EmployeeTechnologyStack> response = repository.findByTechnologyAndEmployee(technology,employee);
        assertTrue(response.get().getEmployee().getId().equals(employeeTechnologyStack.getEmployee().getId()));
    }

    private EmployeeTechnologyStack makeEntity() throws ParseException {
        Employee employee = Employee.builder()
                .uid("22")
                .email("adsa@yahoo.com")
                .name("aaa")
                .workingHours(4)
                .active(true)
                .internalPosition("aaaa")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();
        Technology technology = Technology.builder()
                .name("tech")
                .externalId("externalId")
                .build();
        EmployeeTechnologyStack employeeTechnologyStack = new EmployeeTechnologyStack();
        employeeTechnologyStack.setEmployee(employee);
        employeeTechnologyStack.setTechnology(technology);
        return employeeTechnologyStack;
    }
}