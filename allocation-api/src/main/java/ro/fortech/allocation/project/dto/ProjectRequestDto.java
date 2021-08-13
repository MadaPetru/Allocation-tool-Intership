package ro.fortech.allocation.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

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
    String client;

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

    @NotNull
    @Length(min = 2, max = 500)
    private String technicalStack;
}