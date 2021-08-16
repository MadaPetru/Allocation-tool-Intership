package ro.fortech.allocation.employeestechnologystack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.employeestechnologystack.model.EmployeeTechnologyStack;
import ro.fortech.allocation.employeestechnologystack.model.EmployeeTechnologyStackKey;
import ro.fortech.allocation.technology.model.Technology;

import java.util.Optional;

@Repository
public interface EmployeeTechnologyStackRepository extends JpaRepository<EmployeeTechnologyStack, EmployeeTechnologyStackKey> {
    Optional<EmployeeTechnologyStack> findByTechnologyAndEmployee(Technology technology, Employee employee);
}
