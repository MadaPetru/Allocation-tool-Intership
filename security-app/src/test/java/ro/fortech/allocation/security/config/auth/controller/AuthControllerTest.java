package ro.fortech.allocation.security.config.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.fortech.allocation.security.config.auth.service.AuthService;
import ro.fortech.allocation.security.dto.JwtResponse;
import ro.fortech.allocation.security.dto.LogInRequest;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void itShouldReturnTheResponseProvidedBelowTest() throws Exception {

        JwtResponse response = JwtResponse.builder()
                .token("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJOb3JtYWxVc2VyT25lIiwiaWF0IjoxNjI3MDM2MDcyLCJleHAiOjE2MjcxMjI0NzJ9.MbKaiNY6D1pT8C9LymEiWEbACBb9I5wJauHM9Te77rJYkauePz8m8cRa848T7S3RZ8Tb-D04duvuN6c0oyO0fA")
                .username("SomeUsernameIGuess")
                .email("SomeEmail@Foobar.com")
                .message("User successfully authenticated!")
                .role("NORMAL_USER")
                .build();

        LogInRequest logInRequest = LogInRequest.builder()
                .username("SomeUsernameIGuess")
                .password("SomeEmail@Foobar.com")
                .build();


        when(authService.authenticateUser(logInRequest)).thenReturn(response);

        String body = objectMapper.writeValueAsString(logInRequest);
        String responseAsJson = objectMapper.writeValueAsString(response);

        mockMvc.perform(
                post("/auth/sign-in").content(body).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseAsJson));
    }
}