package ro.fortech.allocation.projecttechnologystack.model;


import lombok.*;
import ro.fortech.allocation.project.model.Project;
import ro.fortech.allocation.technology.model.Technology;

import javax.persistence.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectTechnologyStack {
    @EmbeddedId
    private ProjectTechnologyStackKey id;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "technology_id")
    private Technology technology;
}