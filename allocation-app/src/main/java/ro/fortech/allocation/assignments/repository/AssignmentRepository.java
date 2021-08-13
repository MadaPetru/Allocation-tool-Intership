package ro.fortech.allocation.assignments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.fortech.allocation.assignments.model.Assignment;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.project.model.Project;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findAssignmentsByEmployee(Employee employee);

    List<Assignment> findAssignmentsByProject(Project project);

    @Query(value = "SELECT sum(a.hours) FROM assignment a WHERE a.employee_id=?1", nativeQuery = true)
    Integer getSumAllocationHoursForEmployee(Long employeeId);

    @Query(value = "SELECT * FROM assignment a WHERE a.employee_id=?1 AND a.project_id=?2", nativeQuery = true)
    List<Assignment> getAllAssignmentsForGivenEmployeeAndProject(Long employee_id, Long project_id);

    @Query(value = "SELECT * FROM assignment a WHERE a.project_id=:project_id", nativeQuery = true)
    Set<Assignment> findAssignmentByProject(@Param("project_id") Long project_id);

    Optional<Assignment> findAssignmentByUid(String uid);
}
