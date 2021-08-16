package ro.fortech.allocation.projecttechnologystack.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
@Builder
public class ProjectTechnologyStackDto {
    @NotEmpty(message = "name can not be empty")
    private String projectExternalId;
    @NotEmpty(message = "name can not be empty")
    private String technologyExternalId;
}
