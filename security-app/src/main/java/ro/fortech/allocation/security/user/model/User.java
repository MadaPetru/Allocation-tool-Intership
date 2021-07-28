package ro.fortech.allocation.security.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.fortech.allocation.security.role.model.Role;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String username;

    @Column(nullable = false, length = 64)
    private String password;

    @Email
    @Column(nullable = false, length = 32)
    private String email;

    @ManyToOne
    private Role role;
}
