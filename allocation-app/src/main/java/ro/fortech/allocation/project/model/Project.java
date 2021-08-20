package ro.fortech.allocation.project.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import ro.fortech.allocation.technology.model.Technology;
import ro.fortech.allocation.assignments.model.Assignment;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@Entity(name = "Project")
@Table(name = "project")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Project {
    @Column(nullable = false, updatable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String client;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(length = 2000)
    private String description;

    @Column(name = "technical_stack", length = 500)
    private String technicalStack;

    @Column(unique = true, name = "external_id", updatable = false)
    private String externalId;

    @OneToMany(mappedBy = "project")
    Set<Assignment> assignments;

    @ManyToMany
    @JoinTable(name ="project_technologies",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    private Set<Technology> technologies ;
}