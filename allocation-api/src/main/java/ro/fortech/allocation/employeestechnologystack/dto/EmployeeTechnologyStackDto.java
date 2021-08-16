package ro.fortech.allocation.employeestechnologystack.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Valid
@Builder
@Data
public class EmployeeTechnologyStackDto {
    @NotEmpty(message = "name can not be empty")
    private String employeeUid;
    @NotEmpty(message = "name can not be empty")
    private String technologyExternalId;
}
