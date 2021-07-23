package ro.fortech.allocation.project.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.stereotype.Component;
import javax.persistence.*;
import java.util.Date;

@Component
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
    @Column(nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String client;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    private Date endDate;

    private String description;

    @Column(name = "technical_stack", length = 500)
    private String technicalStack;

    @Column(unique = true, name = "external_id")
    private String externalId;
}

