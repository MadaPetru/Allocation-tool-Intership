package ro.fortech.allocation.employees.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmployeeEmailDto {
    private String uid;

    @NotNull
    private String email;
}
