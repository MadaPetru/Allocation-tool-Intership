package ro.fortech.allocation.employees.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
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
