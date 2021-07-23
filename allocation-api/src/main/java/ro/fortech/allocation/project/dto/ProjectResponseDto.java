package ro.fortech.allocation.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProjectResponseDto {
    private String externalId;

    @NotNull
    String name;

    @NotNull
    String client;

    @NotNull
    Date startDate;

    @NotNull
    Date endDate;

    String description;

    @NotNull
    String technicalStack;
}
