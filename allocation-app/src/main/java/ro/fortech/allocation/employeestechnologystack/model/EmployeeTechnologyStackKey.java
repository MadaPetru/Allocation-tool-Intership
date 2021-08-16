package ro.fortech.allocation.employeestechnologystack.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeTechnologyStackKey implements Serializable {
    @Column(name = "technology_id")
    public Long technologyId;
    @Column(name = "employee_id")
    public Long employeeId;
}
