package ro.fortech.allocation;

import ro.fortech.allocation.project.dto.ProjectRequestDto;
import ro.fortech.allocation.project.dto.ProjectResponseDto;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.technology.dto.TechnologyDto;
import ro.fortech.allocation.technology.model.Technology;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

public class ProjectFactory {
    public static Project getProject() throws ParseException {
        return Project.builder()
                .name("nume")
                .client("client")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .description("good project")
                .externalId("AAA1122")
                .technologies(getSetTechnology())
                .build();
    }

    public static ProjectRequestDto getProjectRequestDto() throws ParseException {
        return ProjectRequestDto.builder()
                .name("nume")
                .client("client")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .description("good project")
                .technologyDtos(getSetTechnologyDto())
                .externalId("AAA1122")
                .build();
    }

    public static ProjectResponseDto getProjectResponseDto() throws ParseException {
        return ProjectResponseDto.builder()
                .name("nume")
                .client("client")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .description("good project")
                .technologyDtos(getSetTechnologyDto())
                .externalId("AAA1122")
                .build();
    }

    public static ProjectRequestDto getInvalidProjectRequestDto() {
        return ProjectRequestDto.builder()
                .name("n")
                .client("")
                .description("")
                .technicalStack("")
                .externalId("AAA1122")
                .build();
    }
    public static Set<TechnologyDto> getSetTechnologyDto(){
        Technology technology = new Technology();
        technology.setName("name");
        technology.setExternalId("externalId");
        TechnologyDto technologyDto = new TechnologyDto();
        technologyDto.setName("name");
        technologyDto.setExternalId("externalId");
        Set<Technology> technologySet = new HashSet<>();
        technologySet.add(technology);
        Set<TechnologyDto> technologySetDto = new HashSet<>();
        technologySetDto.add(technologyDto);
        return technologySetDto;
    }
    public static Set<Technology> getSetTechnology(){
        Technology technology = new Technology();
        technology.setName("name");
        technology.setExternalId("externalId");
        TechnologyDto technologyDto = new TechnologyDto();
        technologyDto.setName("name");
        technologyDto.setExternalId("externalId");
        Set<Technology> technologySet = new HashSet<>();
        technologySet.add(technology);
        Set<TechnologyDto> technologySetDto = new HashSet<>();
        technologySetDto.add(technologyDto);
        return technologySet;
    }

}