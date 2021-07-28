package ro.fortech.allocation.security.role.repository;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.fortech.allocation.security.role.model.ERole;
import ro.fortech.allocation.security.role.model.Role;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();
    }

    @Test
    public void shouldReturnTheRoleWithSpecifiedNameTest() {
        Role defaultRole = Role.builder()
                .name(ERole.NORMAL_USER)
                .build();

        Role savedDefaultRole = roleRepository.save(defaultRole);

        Optional<Role> foundRole = roleRepository.findByName(ERole.NORMAL_USER);

        Assertions.assertTrue(foundRole.isPresent() && foundRole.get().equals(savedDefaultRole));
    }
}