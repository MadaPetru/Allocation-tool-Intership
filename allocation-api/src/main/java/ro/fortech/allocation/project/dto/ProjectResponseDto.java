package ro.fortech.allocation.project.dto;

import com.sun.istack.NotNull;
import lombok.*;
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
