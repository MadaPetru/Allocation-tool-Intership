package ro.fortech.allocation.employees.model;

import lombok.*;
import ro.fortech.allocation.assignments.model.Assignment;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Employee")
public class Employee {
    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true, updatable = false)
    private Long id;
    @Column(nullable = false, unique = true, updatable = false)
    private String uid;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, name = "email")
    private String email;
    @Column(nullable = false)
    private String internalPosition;
    @Column
    private String technicalExpertise;
    @Column
    private String unit;
    @Column
    private String businessUnit;
    @Column
    private String supervisor;
    @Column(nullable = false)
    private Boolean active;
    @Column(nullable = false)
    private Integer workingHours;
    @Column(nullable = false)
    private Date startDate;
    @Column(nullable = false)
    private Date endDate;
    @OneToMany(mappedBy = "employee")
    Set<Assignment> assignments;
}
