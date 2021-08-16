package ro.fortech.allocation.projecttechnologystack.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.technology.model.Technology;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
@Builder
public class ProjectTechnologyStackKey implements Serializable {
    @Column(name = "project_id")
    public Long project_id;
    @Column(name = "technology_id")
    public Long technology_id;
}
