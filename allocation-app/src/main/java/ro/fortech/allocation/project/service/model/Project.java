package ro.fortech.allocation.project.service.model;

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
    String name;

    @Column(nullable = false)
    String client;

    @Column(name = "start_date")
    Date startDate;

    @Column(name = "end_date")
    Date endDate;

    String description;

    @Column(name = "technical_stack", length = 500)
    String technicalStack;

    @Column(unique = true, name = "external_id")
    private String externalId;
}

