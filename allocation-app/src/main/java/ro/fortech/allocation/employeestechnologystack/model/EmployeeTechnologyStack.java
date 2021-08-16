package ro.fortech.allocation.employeestechnologystack.model;


import lombok.*;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.technology.model.Technology;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeTechnologyStack{
    @EmbeddedId
    private EmployeeTechnologyStackKey id;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @MapsId("technologyId")
    @JoinColumn(name = "technology_id")
    private Technology technology;
}
