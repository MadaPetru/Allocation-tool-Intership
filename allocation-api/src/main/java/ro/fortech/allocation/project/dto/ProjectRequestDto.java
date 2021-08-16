package ro.fortech.allocation.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ro.fortech.allocation.technology.dto.TechnologyDto;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProjectRequestDto {
    private String externalId;

    @NotNull
    @Length(min = 2, max = 255)
    private String name;

    @NotNull
    @Length(min = 2, max = 255)
    private String client;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Length(min = 2, max = 2000)
    private String description;

    @Length(min = 2, max = 500)
    private String technicalStack;

    @NotNull
    private Set<TechnologyDto> technologyDtos;

}