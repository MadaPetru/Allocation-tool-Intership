package ro.fortech.allocation.employees.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.fortech.allocation.employees.model.Employee;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findEmployeeByUid(String uid);

    @Query(value = "select * from Employee a where a.email LIKE  %:email% LIMIT 20", nativeQuery = true)
    List<Employee> findEmployeeByEmail(@Param("email") String email);

}
