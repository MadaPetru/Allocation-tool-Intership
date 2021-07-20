package ro.fortech.allocation.employees.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.fortech.allocation.employees.model.Employee;

@Repository
public interface EmployeeRepository  extends JpaRepository<Employee,Long> {

}
