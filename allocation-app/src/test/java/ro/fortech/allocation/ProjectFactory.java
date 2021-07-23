package ro.fortech.allocation;

import ro.fortech.allocation.project.dto.ProjectRequestDto;
import ro.fortech.allocation.project.dto.ProjectResponseDto;
import ro.fortech.allocation.project.model.Project;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ProjectFactory {
    public Project getProject() throws ParseException {
        return Project.builder()
                .name("nume")
                .client("client")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .description("good project")
                .technicalStack(".net")
                .externalId("AAA1122")
                .build();
    }

    public ProjectRequestDto getProjectRequestDto() throws ParseException {
        return ProjectRequestDto.builder()
                .name("nume")
                .client("client")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .description("good project")
                .technicalStack(".net")
                .externalId("AAA1122")
                .build();
    }

    public ProjectResponseDto getProjectResponseDto() throws ParseException {
        return ProjectResponseDto.builder()
                .name("nume")
                .client("client")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .description("good project")
                .technicalStack(".net")
                .externalId("AAA1122")
                .build();
    }
}