package ro.fortech.allocation.project.dto;

import lombok.*;
import ro.fortech.allocation.assignments.dto.ProjectAssignmentDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProjectAssignmentsDto {
    @NotNull
    private String projectExternalId;

    @NotNull
    private String projectName;

    private List<ProjectAssignmentDto> assignments;
}
