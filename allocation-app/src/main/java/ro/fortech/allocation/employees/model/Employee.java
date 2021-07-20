package ro.fortech.allocation.employees.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee {
    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true, updatable = false)
    private Long id;
    @Column(nullable = false, unique = true, updatable = false)
    private String uid;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
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
}
