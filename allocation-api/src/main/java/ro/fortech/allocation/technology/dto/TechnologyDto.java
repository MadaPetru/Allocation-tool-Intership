package ro.fortech.allocation.technology.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class TechnologyDto {
    @NotEmpty(message = "name can't be null or empty")
    private String name;
    private String externalId;
}
