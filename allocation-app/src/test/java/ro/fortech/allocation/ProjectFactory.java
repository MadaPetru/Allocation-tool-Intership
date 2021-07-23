package ro.fortech.allocation;

import ro.fortech.allocation.project.dto.ProjectRequestDto;
import ro.fortech.allocation.project.dto.ProjectResponseDto;
import ro.fortech.allocation.project.service.model.Project;
import java.util.Date;

public class ProjectFactory {

    public Project getProject() {

        return Project.builder()
                .name("nume")
                .client("client")
                .startDate(new Date())
                .endDate(new Date())
                .description("good project")
                .technicalStack(".net")
                .externalId("AAA1122")
                .build();
    }

    public ProjectRequestDto getProjectRequestDto() {
        return ProjectRequestDto.builder()
                .name("nume")
                .client("client")
                .startDate(new Date())
                .endDate(new Date())
                .description("good project")
                .technicalStack(".net")
                .externalId("AAA1122")
                .build();
    }

    public ProjectResponseDto getProjectResponseDto()  {
        return ProjectResponseDto.builder()
                .name("nume")
                .client("client")
                .startDate(new Date())
                .endDate(new Date())
                .description("good project")
                .technicalStack(".net")
                .externalId("AAA1122")
                .build();
    }
}