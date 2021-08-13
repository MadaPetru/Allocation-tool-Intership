package ro.fortech.allocation.assignments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Valid
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDto {
    private String uid;

    @NotEmpty
    private String employeeUid;
    @NotEmpty
    private String projectUid;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    @NotEmpty
    private String projectPosition;
    @NotNull
    @Min(1)
    private Integer allocationHours;
}
