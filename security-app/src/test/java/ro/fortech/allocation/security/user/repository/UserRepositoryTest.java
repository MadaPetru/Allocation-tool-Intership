package ro.fortech.allocation.security.user.repository;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.fortech.allocation.security.user.model.User;

import java.util.Optional;


@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void shouldReturnTheUserWithTheSpecifiedUsernameTest() {
        User user = User.builder()
                .username("SomeRandomUsernameWithNoPurpose")
                .email("Foo@Bar.com")
                .password("SomeRandomPasswordWithNoPurpose123")
                .build();

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());

        Assertions.assertTrue(foundUser.isPresent() && foundUser.get().getUsername().equals(user.getUsername()));
    }

    @Test
    public void shouldReturnTrueIfTheUserWithTheSameUsernameExistsTest() {
        User user = User.builder()
                .username("SomeRandomUsernameWithNoPurpose")
                .email("Foo@Bar.com")
                .password("SomeRandomPasswordWithNoPurpose123")
                .build();

        User savedRole = userRepository.save(user);

        boolean shouldBeTrue = userRepository.existsByUsername(savedRole.getUsername());

        Assertions.assertTrue(shouldBeTrue);
    }

    @Test
    public void shouldReturnTrueIfTheUserWithTheSameEmailExistsTest() {
        User user = User.builder()
                .username("SomeRandomUsernameWithNoPurpose")
                .email("Foo@Bar.com")
                .password("SomeRandomPasswordWithNoPurpose123")
                .build();

        User savedRole = userRepository.save(user);

        boolean shouldBeTrue = userRepository.existsByEmail(savedRole.getEmail());

        Assertions.assertTrue(shouldBeTrue);
    }
}