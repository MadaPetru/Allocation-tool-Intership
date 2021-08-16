package ro.fortech.allocation.technology.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "technologies")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Technology {
    @Column(nullable = false, unique = true)
    private String name;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true,nullable = false)
    private String externalId;
}