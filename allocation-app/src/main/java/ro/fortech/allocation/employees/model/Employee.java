package ro.fortech.allocation.employees.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String internalPosition;
    private String technicalExpertise;
    private String unit;
    private String businessUnit;
    private String supervisor;
    private Boolean active;
    private Integer workingHours;
    private Date startDate;
    private Date endDate;
}
