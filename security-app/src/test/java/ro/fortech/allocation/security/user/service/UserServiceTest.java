package ro.fortech.allocation.security.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.fortech.allocation.security.TestCreationFactory;
import ro.fortech.allocation.security.role.repository.RoleRepository;
import ro.fortech.allocation.security.user.api.dto.UserDto;
import ro.fortech.allocation.security.user.mapper.UserMapper;
import ro.fortech.allocation.security.user.model.User;
import ro.fortech.allocation.security.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userService).build();
    }

    @Test
    public void shouldReturnTheSizeOfTheStoredUsersTest() {
        List<User> users = TestCreationFactory.listOf(User.class);

        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> resultedValue = userService.getAllUsers();

        Assertions.assertEquals(users.size(), resultedValue.size());
    }

    @Test
    public void shouldReturnTheUserAtTheSpecifiedIDTest() {
        long someID = TestCreationFactory.randomLong();
        User user = User.builder()
                .id(someID)
                .username("SomeUsername")
                .email("SomeEmail")
                .password("SomePassword")
                .build();
        UserDto userDTO = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);

        UserDto resultedValue = userService.getUserAtId(someID);

        Assertions.assertEquals(userDTO, resultedValue);
    }
}