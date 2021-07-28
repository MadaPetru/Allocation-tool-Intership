package ro.fortech.allocation.security.user.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.fortech.allocation.security.user.api.dto.UserDto;
import ro.fortech.allocation.security.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void shouldReturnTheListOfUserDTOsTest() throws Exception {
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(new UserDto(1L, "user1", "password1", "foo1@bar.com", "NORMAL_USER"));
        userDtos.add(new UserDto(2L, "user2", "password2", "foo2@bar.com", "MANAGER"));
        userDtos.add(new UserDto(3L, "user3", "password3", "foo3@bar.com", "ADMINISTRATOR"));

        when(userService.getAllUsers()).thenReturn(userDtos);

        String resultedJson = mapper.writeValueAsString(userDtos);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(resultedJson, true));
    }

    @Test
    public void shouldReturnTheUserDTOAtTheSpecifiedIDTest() throws Exception {
        UserDto userDTO = UserDto.builder()
                .id(125L)
                .username("SomeRandomUsernameWithNoPurpose")
                .email("Foo@Bar.com")
                .password("SomeRandomPasswordWithNoPurpose123")
                .role("ADMINISTRATOR")
                .build();

        when(userService.getUserAtId(userDTO.getId())).thenReturn(userDTO);

        String resultedJson = mapper.writeValueAsString(userDTO);

        mockMvc.perform(get("/users/" + userDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(resultedJson, true));
    }
}