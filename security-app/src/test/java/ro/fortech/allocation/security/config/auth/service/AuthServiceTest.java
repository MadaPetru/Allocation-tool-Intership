package ro.fortech.allocation.security.config.auth.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.fortech.allocation.security.config.jwt.JwtUtils;
import ro.fortech.allocation.security.config.user.UserDetailsImpl;
import ro.fortech.allocation.security.dto.JwtResponse;
import ro.fortech.allocation.security.dto.LogInRequest;
import ro.fortech.allocation.security.role.model.ERole;
import ro.fortech.allocation.security.role.model.Role;
import ro.fortech.allocation.security.user.model.User;

import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void authenticateUser() {
        Authentication authentication = mock(Authentication.class, "authentication");

        User user = User.builder()
                .username("SomeUsernameTest")
                .password("SomePasswordTest")
                .email("Email@foobar.com")
                .role(Role.builder().name(ERole.NORMAL_USER).build())
                .build();

        GrantedAuthority normalUserAuthority = new SimpleGrantedAuthority("NORMAL_USER");

        UserDetailsImpl userDetails = UserDetailsImpl.build(
                user
        );

        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IlNvbWVVc2VybmFtZVRlc3QiLCJhZG1pbiI6dHJ1ZSwiaWF0IjoxNTE2MjM5MDIyfQ.bG_CMu0yNCcxbE4FZ_BDb_jtoNZ5AD49KYlA5ay_bTnn9VpF5Yi4amP4YxxZ2q10gp_PHy7P-9wLqbsRGqHFVA");

        when(authentication.getPrincipal()).thenReturn(userDetails);

        JwtResponse response = authService.authenticateUser(
                LogInRequest.builder()
                        .username("SomeUsernameTest")
                        .password("SomePasswordTest")
                        .build()
        );

        assertEquals(response.getToken(), "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IlNvbWVVc2VybmFtZVRlc3QiLCJhZG1pbiI6dHJ1ZSwiaWF0IjoxNTE2MjM5MDIyfQ.bG_CMu0yNCcxbE4FZ_BDb_jtoNZ5AD49KYlA5ay_bTnn9VpF5Yi4amP4YxxZ2q10gp_PHy7P-9wLqbsRGqHFVA");
        assertEquals(userDetails.getEmail(), response.getEmail());
        assertEquals(userDetails.getUsername(), response.getUsername());
        assertEquals(userDetails.getAuthorities(), new HashSet<>(asList(normalUserAuthority)));
    }
}